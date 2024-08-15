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
import com.saucelabs.saucerest.MoshiSingleton;
import com.saucelabs.saucerest.SauceREST;
import com.saucelabs.saucerest.model.jobs.JobAssets;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Log4j2
public class AssetsCommand implements PluginCommand<Object> {

  private final RestClient restClient;

  public AssetsCommand(RestClient restClient) {
    this.restClient = restClient;
  }

  @SneakyThrows
  @Override
  public Object executeCommand(Integration integration, Map<String, Object> params) {
    ValidationUtils.validateParams(params);
    SauceREST sauce =
        restClient.buildSauceClient(integration, (String) params.get(DATA_CENTER.getName()));
    String jobId = (String) params.get(JOB_ID);
    try {
      JobAssets jobAssets = sauce.getJobsEndpoint().listJobAssets(jobId);
      if (jobAssets == null) {
        throw new ReportPortalException(
            ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            Suppliers.formattedSupplier("Job '{}' not found.", jobId)
        );
      }

      String assetsPrefix =
          sauce.getAppServer() + "rest/v1/" + sauce.getUsername() + "/jobs/" + jobId + "/assets/";
      jobAssets.getAvailableAssets()
          .put(assetsPrefix, assetsPrefix);
      JSONObject response = new JSONObject(toJson(jobAssets));
      response.put("assetsPrefix", assetsPrefix);

      return new ObjectMapper().readValue(response.toString(), Object.class);

    } catch (IOException e) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          StringUtils.normalizeSpace(e.getMessage()));
    }
  }

  @Override
  public String getName() {
    return "assets";
  }


  public String toJson(JobAssets jobAssets) {
    Moshi moshi = MoshiSingleton.getInstance();

    JsonAdapter<JobAssets> jsonAdapter = moshi.adapter(JobAssets.class).nonNull();
    return jsonAdapter.toJson(jobAssets);
  }
}
