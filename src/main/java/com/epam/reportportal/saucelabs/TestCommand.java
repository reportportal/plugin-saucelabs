package com.epam.reportportal.saucelabs;

import com.epam.ta.reportportal.entity.integration.Integration;
import com.saucelabs.saucerest.SauceREST;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static com.epam.reportportal.saucelabs.SaucelabsExtension.DATA_CENTER;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class TestCommand implements com.epam.reportportal.extension.PluginCommand<Boolean> {

	@Override
	public Boolean executeCommand(Integration integration, Map params) {
		SauceREST sauce = RestClient.buildSauceClient(integration, (String) params.get(DATA_CENTER));
		String username = sauce.getUsername();
		return StringUtils.isNotEmpty(username);
	}
}
