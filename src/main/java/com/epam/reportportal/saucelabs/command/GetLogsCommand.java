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

import static com.epam.reportportal.saucelabs.model.Constants.GET_RDC_LOGS;
import static com.epam.reportportal.saucelabs.model.Constants.GET_VDC_JOB_ASSETS;
import static com.epam.reportportal.saucelabs.model.Constants.GET_VDC_JOB_LOGS;
import static com.epam.reportportal.saucelabs.model.Constants.JOB_ID;
import static com.epam.reportportal.saucelabs.model.Constants.LOG_URL;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.reportportal.saucelabs.model.IntegrationProperties;
import com.epam.reportportal.saucelabs.utils.ValidationUtils;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class GetLogsCommand implements PluginCommand<Object> {

  private final RestClientBuilder restClient;

  public GetLogsCommand(RestClientBuilder restClient) {
    this.restClient = restClient;
  }

  @SneakyThrows
  @Override
  public Object executeCommand(Integration integration, Map<String, Object> params) {
    ValidationUtils.validateJobId(params);
    ValidationUtils.validateIntegrationParams(integration.getParams());

    IntegrationProperties sp = new IntegrationProperties(integration.getParams().getParams());
    sp.setJobId((String) params.get(JOB_ID));
    RestTemplate restTemplate = restClient.buildRestTemplate(sp);

    try {
      // check if logs exist
      String assetsUrl = String.format(GET_VDC_JOB_ASSETS, sp.getUsername(), sp.getJobId());
      String jobAssets = restTemplate.getForObject(assetsUrl, String.class);
      JsonObject jsonElement = (JsonObject) JsonParser.parseString(jobAssets);
      if (jsonElement.get(LOG_URL) == null) {
        throw new ReportPortalException(ErrorType.NOT_FOUND, "Logs");
      }

      String logsUrl = String.format(GET_VDC_JOB_LOGS, sp.getUsername(), sp.getJobId());
      return restTemplate.getForObject(logsUrl, Object.class);
    } catch (HttpClientErrorException httpException) {
      try {
        String realDeviceJobUrl = String.format(GET_RDC_LOGS, sp.getJobId());
        String jobInfo = restTemplate.getForObject(realDeviceJobUrl, String.class);

        return new ObjectMapper().readValue(jobInfo, Object.class);
      } catch (HttpClientErrorException rdcException) {
        throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            StringUtils.normalizeSpace("Failed to retrieve job logs"));
      }
    }

  }

  @Override
  public String getName() {
    return "logs";
  }


}
