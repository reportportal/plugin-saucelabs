package com.epam.reportportal.saucelabs.command;


import static com.epam.reportportal.saucelabs.model.Constants.JOB_ID;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.DATA_CENTER;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.USERNAME;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import java.util.HashMap;
import java.util.Map;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;

public class GetVirtualDeviceJobCommandTest {

  private static final Map<String, Object> COMMAND_PARAMS = new HashMap<>();
  private static final Map<String, Object> INTEGRATION_PARAMS = new HashMap<>();

  static {
    INTEGRATION_PARAMS.put(USERNAME.getName(), "");
    INTEGRATION_PARAMS.put(ACCESS_TOKEN.getName(), "");
    INTEGRATION_PARAMS.put(DATA_CENTER.getName(), "EU");

    COMMAND_PARAMS.put(JOB_ID, "03afb43944b849e1a9cf68989222037c");

  }

  @Test
  void executeCommand() {
    BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
    Integration integration = new Integration();

    integration.setParams(new IntegrationParams(INTEGRATION_PARAMS));

    GetVirtualDeviceJobCommand command = new GetVirtualDeviceJobCommand(
        new RestClientBuilder(basicTextEncryptor));
    Object o = command.executeCommand(integration, COMMAND_PARAMS);

    assertNotNull(o);
  }
}
