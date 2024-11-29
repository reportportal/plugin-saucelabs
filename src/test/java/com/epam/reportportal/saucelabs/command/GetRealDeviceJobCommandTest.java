package com.epam.reportportal.saucelabs.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

class GetRealDeviceJobCommandTest extends BaseCommandTest {

  @Test
  @DisabledIf("disabled")
  void getRealDeviceJob() {
    GetRealDeviceJobCommand command = new GetRealDeviceJobCommand(
        new RestClientBuilder(basicTextEncryptor));
    Object response = command.executeCommand(INTEGRATION, RDC_COMMAND_PARAMS);

    assertNotNull(response);
  }
}
