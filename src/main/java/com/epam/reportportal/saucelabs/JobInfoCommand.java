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

package com.epam.reportportal.saucelabs;

import static com.epam.reportportal.saucelabs.SaucelabsExtension.JOB_ID;
import static com.epam.reportportal.saucelabs.SaucelabsProperties.DATA_CENTER;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.ta.reportportal.commons.validation.Suppliers;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.reporting.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucelabs.saucerest.SauceREST;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class JobInfoCommand implements PluginCommand<Object> {

  private final RestClient restClient;

  public JobInfoCommand(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public Object executeCommand(Integration integration, Map params) {
    ValidationUtils.validateParams(params);
    SauceREST sauce =
        restClient.buildSauceClient(integration, (String) params.get(DATA_CENTER.getName()));
    try {
      String jobId = (String) params.get(JOB_ID);
      String jobInfo = sauce.getJobInfo(jobId);
      if (StringUtils.isEmpty(jobInfo)) {
        throw new ReportPortalException(
            ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            Suppliers.formattedSupplier("Job '{}' not found.", jobId)
        );
      }
      return new ObjectMapper().readValue(jobInfo, Object.class);
    } catch (IOException e) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, e.getMessage());
    }
  }

  @Override
  public String getName() {
    return "jobInfo";
  }
}
