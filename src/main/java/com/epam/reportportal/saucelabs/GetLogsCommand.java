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
import static com.saucelabs.saucerest.TestAsset.SAUCE_LOG;

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.rules.commons.validation.Suppliers;
import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucelabs.saucerest.SauceREST;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class GetLogsCommand implements PluginCommand<Object> {

  private final RestClient restClient;

  public GetLogsCommand(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public Object executeCommand(Integration system, Map<String, Object> params) {
    ValidationUtils.validateParams(params);
    SauceREST sauce =
        restClient.buildSauceClient(system, (String) params.get(DATA_CENTER.getName()));
    try {
      String jobId = (String) params.get(JOB_ID);
      String logFileName = sauce.getJobsEndpoint().listJobAssets(jobId).sauceLog;
      if (StringUtils.isEmpty(logFileName)) {
        throw new ReportPortalException(
            ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            Suppliers.formattedSupplier("Resource '{}' not found.", jobId)
        );
      }

      Path saveToPath = Path.of(UUID.randomUUID() + "_" + logFileName);
      sauce.getJobsEndpoint().downloadJobAsset(jobId, saveToPath, SAUCE_LOG);

      FileInputStream fis = new FileInputStream(saveToPath.toFile());
      String fileContent = IOUtils.toString(fis, StandardCharsets.UTF_8);
      FileUtils.delete(saveToPath.toFile());

      return new ObjectMapper().readValue(fileContent, Object.class);
    } catch (IOException e) {
      throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
          StringUtils.normalizeSpace(e.getMessage()));
    }
  }

  @Override
  public String getName() {
    return "logs";
  }
}
