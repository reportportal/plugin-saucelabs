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

import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Extension
@Component
public class SaucelabsExtension implements ReportPortalExtensionPoint {

	private static final Map<String, PluginCommand> MAPPING = ImmutableMap.<String, PluginCommand>builder().put("logs",
			new GetLogsCommand()
	)
			.put("jobInfo", new JobInfoCommand())
			.put("testConnection", new TestCommand())
			.put("assets", new AssetsCommand())
			.put("token", new GenerateAuthTokenCommand())
			.build();

	static final String JOB_ID = "jobId";

	@Override
	public Map<String, ?> getPluginParams() {
		Map<String, Object> params = new HashMap<>();
		params.put(ALLOWED_COMMANDS, new ArrayList<>(MAPPING.keySet()));
		params.put("dataCenters", Arrays.stream(DataCenter.values()).map(Enum::toString).collect(Collectors.toList()));
		return params;
	}

	@Override
	public PluginCommand getCommandToExecute(String commandName) {
		return MAPPING.get(commandName);
	}
}
