package com.epam.reportportal.saucelabs;

import static com.epam.reportportal.saucelabs.SaucelabsExtension.JOB_ID;
import static com.epam.reportportal.saucelabs.SaucelabsProperties.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.SaucelabsProperties.USERNAME;
import static com.epam.reportportal.saucelabs.ValidationUtils.validateParams;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.reporting.ErrorType;
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
public class GenerateAuthTokenCommand implements PluginCommand<Object> {

  private final BasicTextEncryptor textEncryptor;

  public GenerateAuthTokenCommand(BasicTextEncryptor textEncryptor) {
    this.textEncryptor = textEncryptor;
  }

  @Override
  public Object executeCommand(Integration integration, Map params) {
    try {
      validateParams(params);

      String username = USERNAME.getParam(integration.getParams()).orElseThrow(
          () -> new ReportPortalException(
				  ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
              "Username is not specified."
          ));
      String accessToken = textEncryptor.decrypt(ACCESS_TOKEN.getParam(integration.getParams())
          .orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
              "Access token is not specified."
          )));

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
