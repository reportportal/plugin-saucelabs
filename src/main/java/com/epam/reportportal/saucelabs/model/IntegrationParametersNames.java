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

package com.epam.reportportal.saucelabs.model;

import com.epam.ta.reportportal.entity.integration.IntegrationParams;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public enum IntegrationParametersNames {

  USERNAME("username"),
  ACCESS_TOKEN("accessToken"),
  DATA_CENTER("dataCenter");

  private final String name;

  IntegrationParametersNames(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getParam(IntegrationParams params) {
    return params.getParams().get(this.name).toString();
  }

}
