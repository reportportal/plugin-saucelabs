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

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.reportportal.saucelabs.model.SauceProperties;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Slf4j
public class AssetsCommand implements PluginCommand<Object> {

	private final RestClientBuilder restClient;

	public AssetsCommand(RestClientBuilder restClient) {
		this.restClient = restClient;
	}

	@SneakyThrows
  @Override
	public Object executeCommand(Integration integration, Map<String, Object> params) {
		ValidationUtils.validateParams(params);
		ValidationUtils.validateIntegrationParams(integration.getParams());

    SauceProperties sp = new SauceProperties(integration.getParams().getParams());
    sp.setJobId((String) params.get(JOB_ID));
    RestTemplate restTemplate = restClient.buildRestTemplate(sp);

    try {
		String url = "/rest/v1/" + sp.getUsername() + "/jobs/" + sp.getJobId() + "/assets";
      String jobAssets = restTemplate.getForObject(url, String.class);
		JSONObject response = new JSONObject(jobAssets);
      response.put("assetsPrefix",
          sp.getDatacenter().apiServer + "rest/v1/" + sp.getUsername() + "/jobs/" + sp.getJobId()
              + "/assets");
		return new ObjectMapper().readValue(response.toString(), Object.class);

    } catch (HttpClientErrorException httpException) {
      if (httpException.getStatusCode().is4xxClientError()) {
        // TODO: handle RD endpoint in a separate plugin command. UI updates required
			//String url = sp.getDatacenter().apiServer + "v1/rdc/jobs/" + sp.getJobId();
        //DeviceJob deviceJob = restTemplate.getForObject(url, DeviceJob.class);

				JSONObject response = new JSONObject();
        response.put("assetsPrefix",
						String.format("%sv1/rdc/jobs/%s/", sp.getDatacenter().apiServer, sp.getJobId()));
				response.put("screenshots", new JSONArray());
			response.put("sauce-log",
			String.format("%sv1/rdc/jobs/%s/deviceLogs", sp.getDatacenter().apiServer,
                sp.getJobId()));
        return new ObjectMapper().readValue(response.toString(), Object.class);

		} else {
			throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, StringUtils.normalizeSpace("Failed to retrieve job assets"));
      }
		}
	}

	@Override
	public String getName() {
		return "assets";
	}
}
