package com.epam.reportportal.saucelabs;

import com.epam.reportportal.extension.saucelabs.SaucelabsExtensionPoint;
import com.epam.ta.reportportal.entity.integration.Integration;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saucelabs.saucerest.SauceREST;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class SaucelabsExtension implements SaucelabsExtensionPoint {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public InputStream downloadVideo(Integration system, String jobId, String dataCenter) {
		SauceREST sauce = RestClient.buildSauceClient(system, dataCenter);
		try {
			return sauce.downloadVideo(jobId);
		} catch (IOException e) {
			throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, e.getMessage());
		}
	}

	@Override
	public Object getLogs(Integration system, String jobId, String dataCenter) {
		SauceREST sauce = RestClient.buildSauceClient(system, dataCenter);
		try {
			return objectMapper.readValue(sauce.retrieveResults(sauce.getUsername() + "/jobs/" + jobId + "assets/log.json"), Object.class);
		} catch (IOException e) {
			throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, e.getMessage());
		}
	}

	@Override
	public Object getJobInfo(Integration system, String jobId, String dataCenter) {
		SauceREST sauce = RestClient.buildSauceClient(system, dataCenter);
		try {
			return objectMapper.readValue(sauce.getJobInfo(jobId), Object.class);
		} catch (IOException e) {
			throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, e.getMessage());
		}
	}

}
