/*
 * Copyright 2024 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.reportportal.saucelabs.command;


import static com.epam.reportportal.saucelabs.model.Constants.JOB_ID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import java.util.HashMap;
import java.util.Map;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

public class GetVirtualDeviceJobCommandTest extends BaseCommandTest {

  @Test
  @DisabledIf("disabled")
  void getVirtualDeviceJob() {
    GetVirtualDeviceJobCommand command = new GetVirtualDeviceJobCommand(
        new RestClientBuilder(basicTextEncryptor));
    Object response = command.executeCommand(INTEGRATION, VDC_COMMAND_PARAMS);

    assertNotNull(response);
  }

  @Test
  @DisabledIf("disabled")
  void getVirtualDeviceJobLogs() {
    GetLogsCommand command = new GetLogsCommand(new RestClientBuilder(basicTextEncryptor));
    Object response = command.executeCommand(INTEGRATION, VDC_COMMAND_PARAMS);

    assertNotNull(response);
  }

  @Test
  @DisabledIf("disabled")
  void getVirtualDeviceJobNotExists() {
    Map<String, Object> params = new HashMap<>();
    params.put(JOB_ID, "not-exists-job-id");

    BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

    GetLogsCommand command = new GetLogsCommand(new RestClientBuilder(basicTextEncryptor));
    Assertions.assertThrows(ReportPortalException.class,
        () -> command.executeCommand(INTEGRATION, params));

  }
}
