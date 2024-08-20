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

package com.epam.reportportal.saucelabs.utils;


import com.epam.ta.reportportal.exception.ReportPortalException;
import com.epam.ta.reportportal.ws.model.ErrorType;
import com.saucelabs.saucerest.DataCenter;

/**
 * Utility class for resolving deprecated datacenter names to their current equivalents. This is the
 * temporary workaround in order to mitigate inconsistency between saved integrations and saucelabs
 * rest client {@link DataCenter} Enum.
 */
// TODO: migrate integration old values and remove this class
public class OldDatacenterResolver {

  private OldDatacenterResolver() {
  }

  /**
   * Resolves the deprecated datacenter name to the current equivalent.
   *
   * @param oldDatacenter the deprecated datacenter name
   * @return the current datacenter name
   * @throws ReportPortalException if the datacenter name is invalid
   */
  public static DataCenter resolveDatacenterDeprecatedName(String oldDatacenter) {
    switch (oldDatacenter) {
      case "EU":
      case "EU_CENTRAL":
        return DataCenter.EU_CENTRAL;
      case "US":
      case "US_WEST":
        return DataCenter.US_WEST;
      default:
        throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            "Invalid SauceLabs Datacenter value");
    }

  }
}
