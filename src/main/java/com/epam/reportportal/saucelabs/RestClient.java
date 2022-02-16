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
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.util.text.BasicTextEncryptor;

import static com.epam.reportportal.saucelabs.SaucelabsProperties.*;
import static java.util.Optional.ofNullable;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class RestClient {

	private final BasicTextEncryptor textEncryptor;

	public RestClient(BasicTextEncryptor textEncryptor) {
		this.textEncryptor = textEncryptor;
	}

	public SauceREST buildSauceClient(Integration system, String dataCenter) {
		IntegrationParams params = ofNullable(system.getParams()).orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
				"Integration params are not specified."
		));

		String username = USERNAME.getParam(params)
				.orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, "Username is not specified."));
		String accessToken = textEncryptor.decrypt(ACCESS_TOKEN.getParam(params)
				.orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
						"Access token is not specified."
				)));
		String dc = ofNullable(dataCenter).orElse(DATA_CENTER.getParam(params)
				.orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, "Data center is not specified.")));

		SauceREST sauceREST = new SauceREST(username, accessToken, DataCenter.fromString(dc));

		if (StringUtils.isEmpty(sauceREST.getUser())) {
			throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, "Incorrect Username or Access token");
		}

		return sauceREST;
	}

}
