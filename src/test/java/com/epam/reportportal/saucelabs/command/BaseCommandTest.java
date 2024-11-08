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
import static com.epam.reportportal.saucelabs.utils.TestProperties.getTestProperties;

import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.BeforeAll;

// set values in 'integration.properties' to enable the tests
public abstract class BaseCommandTest {


  public static final Map<String, Object> VDC_COMMAND_PARAMS = new HashMap<>();
  public static final Map<String, Object> RDC_COMMAND_PARAMS = new HashMap<>();
  public static final String VDC_JOB_ID = "vdcJobId";
  public static final String RDC_JOB_ID = "rdcJobId";
  public static final Integration INTEGRATION = new Integration();

  BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();

  @BeforeAll
  protected static void before() {
    Properties integrationProps = getTestProperties("integration.properties");
    Map<String, Object> params = new HashMap<>();
    integrationProps.keySet()
        .stream()
        .map(String::valueOf)
        .forEach(key -> params.put(key, integrationProps.getProperty(key)));
    INTEGRATION.setParams(new IntegrationParams(params));

    Properties jobProps = getTestProperties("jobs.properties");
    VDC_COMMAND_PARAMS.put(JOB_ID, jobProps.get(VDC_JOB_ID));
    RDC_COMMAND_PARAMS.put(JOB_ID, jobProps.get(RDC_JOB_ID));

  }

  protected boolean disabled() {
    return INTEGRATION.getParams().getParams().values()
        .stream()
        .map(String::valueOf)
        .anyMatch(StringUtils::isEmpty);
  }

}
