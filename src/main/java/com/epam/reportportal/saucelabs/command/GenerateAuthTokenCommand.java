package com.epam.reportportal.saucelabs.command;

import static com.epam.reportportal.saucelabs.model.Constants.JOB_ID;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.USERNAME;
import static com.epam.reportportal.saucelabs.utils.ValidationUtils.validateIntegrationParams;

import com.epam.reportportal.api.model.PluginCommandRQ;
import com.epam.reportportal.base.infrastructure.persistence.dao.ProjectRepository;
import com.epam.reportportal.base.infrastructure.persistence.dao.ProjectUserRepository;
import com.epam.reportportal.base.infrastructure.persistence.dao.organization.OrganizationRepository;
import com.epam.reportportal.base.infrastructure.persistence.dao.organization.OrganizationUserRepository;
import com.epam.reportportal.base.infrastructure.persistence.entity.integration.Integration;
import com.epam.reportportal.base.infrastructure.rules.exception.ReportPortalException;
import com.epam.reportportal.extension.command.AbstractExtensionCommand;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class GenerateAuthTokenCommand extends AbstractExtensionCommand<Object> {

  private final BasicTextEncryptor textEncryptor;

  public GenerateAuthTokenCommand(BasicTextEncryptor textEncryptor,
      ProjectRepository projectRepository,
      OrganizationUserRepository organizationUserRepository,
      OrganizationRepository organizationRepository,
      ProjectUserRepository projectUserRepository) {
    super(projectRepository, organizationUserRepository, organizationRepository,
        projectUserRepository);
    this.textEncryptor = textEncryptor;
  }

  @Override
  public Object executeCommand(Integration integration, PluginCommandRQ pluginCommandRq) {
    Map<String, Object> params = pluginCommandRq.getArguments();
    try {
      validateIntegrationParams(integration.getParams());

      String username = USERNAME.getParam(integration.getParams());
      String accessToken = textEncryptor.decrypt(ACCESS_TOKEN.getParam(integration.getParams()));

      SecretKeySpec keySpec =
          new SecretKeySpec((username + ":" + accessToken).getBytes(StandardCharsets.UTF_8),
              "HmacMD5"
          );
      Mac mac = Mac.getInstance("HmacMD5");
      mac.init(keySpec);
      return Collections.singletonMap("token", Hex.encodeHexString(
          mac.doFinal(params.get(JOB_ID).toString().getBytes(StandardCharsets.UTF_8))));
    } catch (Exception e) {
      throw new ReportPortalException(e.getMessage());
    }
  }

  @Override
  public String getName() {
    return "token";
  }
}
