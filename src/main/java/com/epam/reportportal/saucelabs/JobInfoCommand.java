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
import com.epam.reportportal.rules.commons.validation.Suppliers;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.model.jobs.Job;
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
      Job jobInfo = sauce.getJobsEndpoint().getJobDetails(jobId);
      if (jobInfo == null) {
        throw new ReportPortalException(
            ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            Suppliers.formattedSupplier("Job '{}' not found.", jobId)
        );
      }
      return new ObjectMapper().readValue(jobInfo.toJson(), Object.class);
    } catch (IOException e) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          StringUtils.normalizeSpace(e.getMessage()));
    }
  }

  @Override
  public String getName() {
    return "jobInfo";
  }
}
