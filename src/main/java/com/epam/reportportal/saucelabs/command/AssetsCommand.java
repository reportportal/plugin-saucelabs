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

import static com.epam.reportportal.saucelabs.model.Constants.GET_VDC_JOB_ASSETS;
import static com.epam.reportportal.saucelabs.model.Constants.JOB_ID;
import static com.epam.reportportal.saucelabs.model.Constants.LOG_URL;

import com.epam.reportportal.api.model.PluginCommandRQ;
import com.epam.reportportal.base.infrastructure.persistence.dao.ProjectRepository;
import com.epam.reportportal.base.infrastructure.persistence.dao.ProjectUserRepository;
import com.epam.reportportal.base.infrastructure.persistence.dao.organization.OrganizationRepository;
import com.epam.reportportal.base.infrastructure.persistence.dao.organization.OrganizationUserRepository;
import com.epam.reportportal.base.infrastructure.persistence.entity.integration.Integration;
import com.epam.reportportal.extension.command.AbstractExtensionCommand;
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.reportportal.saucelabs.model.IntegrationProperties;
import com.epam.reportportal.saucelabs.utils.ValidationUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import tools.jackson.databind.json.JsonMapper;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Slf4j
public class AssetsCommand extends AbstractExtensionCommand<Object> {

  private final RestClientBuilder restClient;

  private final JsonMapper jsonMapper;

  public AssetsCommand(RestClientBuilder restClient, JsonMapper jsonMapper,
      ProjectRepository projectRepository,
      OrganizationUserRepository organizationUserRepository,
      OrganizationRepository organizationRepository,
      ProjectUserRepository projectUserRepository) {
    super(projectRepository, organizationUserRepository, organizationRepository,
        projectUserRepository);
    this.restClient = restClient;
    this.jsonMapper = jsonMapper;
  }

  @Override
  public Object executeCommand(Integration integration, PluginCommandRQ pluginCommandRq) {
    Map<String, Object> params = pluginCommandRq.getArguments();
    ValidationUtils.validateIntegrationParams(integration.getParams());
    ValidationUtils.validateJobId(params);

    IntegrationProperties sp = new IntegrationProperties(integration.getParams().getParams());
    sp.setJobId((String) params.get(JOB_ID));
    RestClient http = restClient.buildRestClient(sp);

    try {
      String assetsUrl = String.format(GET_VDC_JOB_ASSETS, sp.getUsername(), sp.getJobId());
      String jobAssets = http.get().uri(assetsUrl).retrieve().body(String.class);

      JsonObject jsonElement = (JsonObject) JsonParser.parseString(jobAssets);
      jsonElement.addProperty("assetsPrefix", sp.getDatacenter().getBaseUrl() + assetsUrl);

      return jsonMapper.readValue(jsonElement.toString(), Object.class); // try skip mapping

    } catch (RestClientException _) {
      String rdcJobUrl = String.format("%s/v1/rdc/jobs/%s", sp.getDatacenter().getBaseUrl(), sp.getJobId());
      Map<String, Object> response = new LinkedHashMap<>();
      response.put("assetsPrefix", rdcJobUrl + "/");
      response.put("screenshots", List.of());
      response.put(LOG_URL, rdcJobUrl + "/deviceLogs");
      return response;
    }
  }

  @Override
  public String getName() {
    return "assets";
  }

}
