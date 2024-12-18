package com.epam.reportportal.saucelabs.command;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

class TestConnectionCommandTest extends BaseCommandTest {

  @Test
  @DisabledIf("disabled")
  void testConnection() {
    TestConnectionCommand command = new TestConnectionCommand(
        new RestClientBuilder(basicTextEncryptor));
    assertTrue(command.executeCommand(INTEGRATION, new HashMap<String, Object>()));
  }
}
