package com.epam.reportportal.saucelabs;

import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.saucelabs.saucerest.DataCenter;
import com.saucelabs.saucerest.SauceREST;

import static com.epam.reportportal.saucelabs.SaucelabsProperties.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.SaucelabsProperties.USERNAME;
import static java.util.Optional.ofNullable;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class RestClient {

	public static SauceREST buildSauceClient(Integration system, String dataCenter) {
		IntegrationParams params = ofNullable(system.getParams()).orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
				"Integration params are not specified."
		));
		String username = USERNAME.getParam(params)
				.orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, "Username is not specified."));
		String accessToken = ACCESS_TOKEN.getParam(params)
				.orElseThrow(() -> new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, "Access token is not specified."));
		return new SauceREST(username, accessToken, DataCenter.fromString(dataCenter));
	}

}
