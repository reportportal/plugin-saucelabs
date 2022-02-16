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

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.google.common.collect.ImmutableMap;
import com.saucelabs.saucerest.DataCenter;
import org.jasypt.util.text.BasicTextEncryptor;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Extension
public class SaucelabsExtension implements ReportPortalExtensionPoint {

	static final String JOB_ID = "jobId";

	private final Supplier<Map<String, PluginCommand<?>>> pluginCommandMapping = new MemoizingSupplier<>(this::getCommands);

	private final Supplier<RestClient> restClientSupplier;

	@Autowired
	private BasicTextEncryptor basicEncryptor;

	public SaucelabsExtension() {
		restClientSupplier = new MemoizingSupplier<>(() -> new RestClient(basicEncryptor));
	}


	@Override
	public Map<String, ?> getPluginParams() {
		Map<String, Object> params = new HashMap<>();
		params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
		params.put("dataCenters", Arrays.stream(DataCenter.values()).map(Enum::toString).collect(Collectors.toList()));
		return params;
	}

	@Override
	public CommonPluginCommand getCommonCommand(String commandName) {
		throw new UnsupportedOperationException("Plugin commands are not supported");
	}

	@Override
	public PluginCommand getIntegrationCommand(String commandName) {
		return pluginCommandMapping.get().get(commandName);
	}

	private Map<String, PluginCommand<?>> getCommands() {
		return ImmutableMap.<String, PluginCommand<?>>builder().put("logs", new GetLogsCommand(restClientSupplier.get()))
				.put("jobInfo", new JobInfoCommand(restClientSupplier.get()))
				.put("testConnection", new TestConnectionCommand(restClientSupplier.get()))
				.put("assets", new AssetsCommand(restClientSupplier.get()))
				.put("token", new GenerateAuthTokenCommand(basicEncryptor))
				.build();

	}
}
