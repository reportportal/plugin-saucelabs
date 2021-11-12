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

import com.epam.ta.reportportal.entity.integration.Integration;
import com.saucelabs.saucerest.SauceREST;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.epam.reportportal.saucelabs.SaucelabsProperties.DATA_CENTER;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class TestConnectionCommand implements com.epam.reportportal.extension.PluginCommand<Boolean> {

	private final RestClient restClient;

	public TestConnectionCommand(RestClient restClient) {
		this.restClient = restClient;
	}

	@Override
	public Boolean executeCommand(Integration integration, Map params) {
		SauceREST sauce = restClient.buildSauceClient(integration, (String) params.get(DATA_CENTER.getName()));
		String username = sauce.getUser();
		return StringUtils.isNotEmpty(username);
	}
}
