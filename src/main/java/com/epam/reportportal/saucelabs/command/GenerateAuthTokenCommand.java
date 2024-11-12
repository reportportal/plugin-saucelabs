package com.epam.reportportal.saucelabs.command;

import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.utils.ValidationUtils.validateIntegrationParams;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.ta.reportportal.entity.integration.Integration;
import java.util.Collections;
import java.util.Map;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class GenerateAuthTokenCommand implements PluginCommand<Object> {

  private final BasicTextEncryptor textEncryptor;

  public GenerateAuthTokenCommand(BasicTextEncryptor textEncryptor) {
    this.textEncryptor = textEncryptor;
  }

  @Override
  public Object executeCommand(Integration integration, Map params) {
    try {
      validateIntegrationParams(integration.getParams());

      String accessToken = textEncryptor.decrypt(ACCESS_TOKEN.getParam(integration.getParams()));

      return Collections.singletonMap("token", accessToken);
    } catch (Exception e) {
      throw new ReportPortalException(e.getMessage());
    }
  }

  @Override
  public String getName() {
    return "token";
  }
}
