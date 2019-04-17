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
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			.put("test", new TestCommand())
			.put("assets", new AssetsCommand())
			.build();

	static final String DATA_CENTER = "dataCenter";
	static final String JOB_ID = "jobId";

	@Override
	public List<String> getCommandNames() {
		return new ArrayList<>(MAPPING.keySet());
	}

	@Override
	public PluginCommand getCommandToExecute(String commandName) {
		return MAPPING.get(commandName);
	}
}
