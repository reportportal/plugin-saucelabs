package com.epam.reportportal.saucelabs.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

class GetLogsCommandTest extends BaseCommandTest {

  @Test
  @DisabledIf("disabled")
  void getVirtualDeviceLogs() {
    GetLogsCommand command = new GetLogsCommand(
        new RestClientBuilder(basicTextEncryptor));
    Object response = command.executeCommand(INTEGRATION, VDC_COMMAND_PARAMS);

    assertNotNull(response);
  }
}
