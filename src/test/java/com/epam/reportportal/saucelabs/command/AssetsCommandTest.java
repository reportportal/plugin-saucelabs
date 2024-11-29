package com.epam.reportportal.saucelabs.command;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

class AssetsCommandTest extends BaseCommandTest {

  @Test
  @DisabledIf("disabled")
  void getJobAssets() {
    AssetsCommand command = new AssetsCommand(
        new RestClientBuilder(basicTextEncryptor));
    Object response = command.executeCommand(INTEGRATION, VDC_COMMAND_PARAMS);

    assertNotNull(response);
  }
}
