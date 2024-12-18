/*
 * Copyright 2024 EPAM Systems
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

package com.epam.reportportal.saucelabs.model;

import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.DATA_CENTER;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.USERNAME;
import static com.epam.reportportal.saucelabs.utils.DatacenterNameResolver.resolveDatacenterName;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class IntegrationProperties {

  private final String username;
  private final String token;
  private final DataCenter datacenter;

  private String jobId;

  public IntegrationProperties(String username, String token, DataCenter datacenter) {
    this.username = username;
    this.token = token;
    this.datacenter = datacenter;
  }

  public IntegrationProperties(Map<String, Object> params) {
    this.username = (String) params.get(USERNAME.getName());
    this.token = (String) params.get(ACCESS_TOKEN.getName());
    this.datacenter = resolveDatacenterName((String) params.get(DATA_CENTER.getName()));
  }
}
