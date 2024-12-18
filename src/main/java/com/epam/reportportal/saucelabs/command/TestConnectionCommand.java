/*
 * Copyright 2019 EPAM Systems
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

import static com.epam.reportportal.saucelabs.model.Constants.TEST_CONNECTION;

import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.reportportal.saucelabs.model.IntegrationProperties;
import com.epam.reportportal.saucelabs.utils.ValidationUtils;
import com.epam.ta.reportportal.entity.integration.Integration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Slf4j
public class TestConnectionCommand implements
    com.epam.reportportal.extension.PluginCommand<Boolean> {

  private final RestClientBuilder restClient;

  public TestConnectionCommand(RestClientBuilder restClient) {
    this.restClient = restClient;
  }

  @Override
  public Boolean executeCommand(Integration integration, Map params) {
    ValidationUtils.validateIntegrationParams(integration.getParams());
    IntegrationProperties sp = new IntegrationProperties(integration.getParams().getParams());
    RestTemplate restTemplate = restClient.buildRestTemplate(sp);

    try {
      String assetsUrl = String.format(TEST_CONNECTION, sp.getUsername());
      restTemplate.getForObject(assetsUrl, String.class);
      return true;
    } catch (Exception e) {
      log.error("Test connection failed", e);
      return false;
    }
  }

  @Override
  public String getName() {
    return "testConnection";
  }
}
