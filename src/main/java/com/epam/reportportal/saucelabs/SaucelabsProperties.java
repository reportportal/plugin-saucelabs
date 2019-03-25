package com.epam.reportportal.saucelabs;

import com.epam.ta.reportportal.entity.integration.IntegrationParams;

import java.util.Optional;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public enum SaucelabsProperties {

	USERNAME("username"),
	ACCESS_TOKEN("accessToken");

	private final String name;

	SaucelabsProperties(String name) {
		this.name = name;
	}

	public Optional<String> getParam(IntegrationParams params) {
		return Optional.ofNullable(params.getParams().get(this.name)).map(String::valueOf);
	}

}
