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
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jasypt.util.text.BasicTextEncryptor;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
@Extension
public class SaucelabsExtension implements ReportPortalExtensionPoint {

	private static final String DOCUMENTATION_LINK_FIELD = "documentationLink";
	private static final String DOCUMENTATION_LINK = "https://reportportal.io/docs/plugins/SauceLabs";
	static final String JOB_ID = "jobId";

	private final Supplier<Map<String, PluginCommand<?>>> pluginCommandMapping = new MemoizingSupplier<>(this::getCommands);

	private final Supplier<SauceRestClient> restClientSupplier;
	private final Supplier<RestClientBuilder> newRestClientSupplier;

	@Autowired
	private BasicTextEncryptor basicEncryptor;

	public SaucelabsExtension() {
		restClientSupplier = new MemoizingSupplier<>(() -> new SauceRestClient(basicEncryptor));
    newRestClientSupplier = new MemoizingSupplier<>(() -> new RestClientBuilder(basicEncryptor));
	}


	@Override
	public Map<String, ?> getPluginParams() {
		Map<String, Object> params = new HashMap<>();
		params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
		params.put(DOCUMENTATION_LINK_FIELD, DOCUMENTATION_LINK);
		//params.put("dataCenters", Arrays.stream(DataCenter.values()).map(Enum::toString).collect(Collectors.toList()));
		params.put("dataCenters", Arrays.asList("US", "EU"));

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
		return ImmutableMap.<String, PluginCommand<?>>builder()
        .put("logs", new GetLogsCommand(newRestClientSupplier.get()))
				.put("jobInfo", new JobInfoCommand(restClientSupplier.get()))
				.put("testConnection", new TestConnectionCommand(restClientSupplier.get()))
				.put("assets", new AssetsCommand(newRestClientSupplier.get()))
				.put("token", new GenerateAuthTokenCommand(basicEncryptor))
				.build();

	}
}
