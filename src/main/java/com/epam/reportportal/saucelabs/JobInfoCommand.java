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
import static com.epam.reportportal.saucelabs.utils.OldDatacenterResolver.resolveDatacenterDeprecatedName;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.reportportal.saucelabs.utils.JsonUtils;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucelabs.saucerest.SauceException;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.model.jobs.Job;
import com.saucelabs.saucerest.model.realdevices.DeviceJob;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Slf4j
public class JobInfoCommand implements PluginCommand<Object> {

  private final SauceRestClient sauceRestClient;

  public JobInfoCommand(SauceRestClient sauceRestClient) {
    this.sauceRestClient = sauceRestClient;
  }

  @Override
  public Object executeCommand(Integration integration, Map params) {
    ValidationUtils.validateParams(params);
    String datacenter = (String) params.get(DATA_CENTER.getName());

    SauceREST sauce =
        sauceRestClient.buildSauceClient(integration, resolveDatacenterDeprecatedName(datacenter));
    String jobId = (String) params.get(JOB_ID);

    try {
      return findJobById(sauce, jobId);
    } catch (IOException e) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          StringUtils.normalizeSpace(e.getMessage()));
    }
  }

  private Object findJobById(SauceREST sauce, String jobId)
      throws IOException {
    Object response;
    try {
      // find job if exists
      Job jobInfo = sauce.getJobsEndpoint().getJobDetails(jobId);
      response = new ObjectMapper().readValue(jobInfo.toJson(), Object.class);
    } catch (SauceException jobException) {
      // If job not exists find real device job
      // TODO: introduce separate plugin command. UI updates required
      DeviceJob dj = sauce.getRealDevicesEndpoint().getSpecificDeviceJob(jobId);
      JsonUtils.toJson(dj, DeviceJob.class);
      response = new ObjectMapper()
          .readValue(JsonUtils.toJson(dj, DeviceJob.class), Object.class);
    }
    return response;
  }

  @Override
  public String getName() {
    return "jobInfo";
  }
}
