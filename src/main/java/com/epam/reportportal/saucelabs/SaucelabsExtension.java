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

import static java.util.stream.Collectors.toList;

import com.epam.reportportal.extension.CommonPluginCommand;
import com.epam.reportportal.extension.PluginCommand;
import com.epam.reportportal.extension.ReportPortalExtensionPoint;
import com.epam.reportportal.saucelabs.client.RestClientBuilder;
import com.epam.reportportal.saucelabs.command.AssetsCommand;
import com.epam.reportportal.saucelabs.command.GenerateAuthTokenCommand;
import com.epam.reportportal.saucelabs.command.GetLogsCommand;
import com.epam.reportportal.saucelabs.command.GetRealDeviceJobCommand;
import com.epam.reportportal.saucelabs.command.GetVirtualDeviceJobCommand;
import com.epam.reportportal.saucelabs.command.TestConnectionCommand;
import com.epam.reportportal.saucelabs.model.DataCenter;
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

  private final Supplier<Map<String, PluginCommand<?>>> pluginCommandMapping = new MemoizingSupplier<>(
      this::getCommands);

  private final Supplier<RestClientBuilder> restClientSupplier;

  @Autowired
  private BasicTextEncryptor basicEncryptor;

  public SaucelabsExtension() {
    restClientSupplier = new MemoizingSupplier<>(() -> new RestClientBuilder(basicEncryptor));
  }


  @Override
  public Map<String, ?> getPluginParams() {
    Map<String, Object> params = new HashMap<>();
    params.put(ALLOWED_COMMANDS, new ArrayList<>(pluginCommandMapping.get().keySet()));
    params.put(DOCUMENTATION_LINK_FIELD, DOCUMENTATION_LINK);
    /*params.put("dataCenters", Arrays.stream(DataCenter.values())
        .map(Enum::toString)
        .collect(toList()));*/
    params.put("dataCenters", Arrays.asList("US", "EU", "US_EAST"));

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
        .put("logs", new GetLogsCommand(restClientSupplier.get()))
        .put("jobInfo", new GetVirtualDeviceJobCommand(restClientSupplier.get()))
        .put("realDeviceJobInfo", new GetRealDeviceJobCommand(restClientSupplier.get()))
        .put("testConnection", new TestConnectionCommand(restClientSupplier.get()))
        .put("assets", new AssetsCommand(restClientSupplier.get()))
        .put("token", new GenerateAuthTokenCommand(basicEncryptor))
        .build();

  }
}
