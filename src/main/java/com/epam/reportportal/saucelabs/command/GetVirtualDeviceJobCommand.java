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

import static com.epam.reportportal.saucelabs.model.Constants.GET_VDC_JOB;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.reportportal.saucelabs.ValidationUtils;
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.reportportal.saucelabs.model.Constants;
import com.epam.reportportal.saucelabs.model.SauceProperties;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Slf4j
public class GetVirtualDeviceJobCommand implements PluginCommand<Object> {

  private final RestClientBuilder restClient;

  public GetVirtualDeviceJobCommand(RestClientBuilder restClient) {
    this.restClient = restClient;
  }

  @SneakyThrows
  @Override
  public Object executeCommand(Integration integration, Map params) {
    ValidationUtils.validateIntegrationParams(integration.getParams());
    ValidationUtils.validateJobId(params);

    SauceProperties sp = new SauceProperties(integration.getParams().getParams());
    sp.setJobId((String) params.get(Constants.JOB_ID));
    RestTemplate restTemplate = restClient.buildRestTemplate(sp);

    try {
      String realDeviceJobUrl = String.format(GET_VDC_JOB, sp.getUsername(), sp.getJobId());
      String jobInfo = restTemplate.getForObject(realDeviceJobUrl, String.class);

      return new ObjectMapper().readValue(jobInfo, Object.class); // try skip mapping

    } catch (HttpClientErrorException httpException) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          StringUtils.normalizeSpace("Failed to retrieve real device job info"));
    }
  }


  @Override
  public String getName() {
    return "jobInfo";
  }
}
