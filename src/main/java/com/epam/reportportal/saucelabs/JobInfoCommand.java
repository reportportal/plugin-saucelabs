package com.epam.reportportal.saucelabs;

import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucelabs.saucerest.SauceREST;

import java.io.IOException;
import java.util.Map;

import static com.epam.reportportal.saucelabs.SaucelabsExtension.DATA_CENTER;
import static com.epam.reportportal.saucelabs.SaucelabsExtension.JOB_ID;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class JobInfoCommand implements com.epam.reportportal.extension.PluginCommand {

	@Override
	public Object executeCommand(Integration integration, Map params) {
		ValidationUtils.validateParams(params);
		SauceREST sauce = RestClient.buildSauceClient(integration, (String) params.get(DATA_CENTER));
		try {
			return new ObjectMapper().readValue(sauce.getJobInfo((String) params.get(JOB_ID)), Object.class);
		} catch (IOException e) {
			throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, e.getMessage());
		}
	}
}
