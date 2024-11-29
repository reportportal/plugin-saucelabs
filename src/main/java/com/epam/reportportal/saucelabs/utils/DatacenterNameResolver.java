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

import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.reportportal.rules.exception.ReportPortalException;
import com.epam.reportportal.saucelabs.model.DataCenter;

/**
 * Utility class for resolving deprecated datacenter names to their current equivalents. This is the
 * temporary workaround in order to mitigate inconsistency between saved integrations and saucelabs
 * rest client {@link DataCenter} Enum.
 */
public class DatacenterNameResolver {

  private DatacenterNameResolver() {
  }

  /**
   * Resolves the deprecated datacenter name to the current equivalent.
   *
   * @param oldDatacenter the deprecated datacenter name
   * @return the current datacenter name
   * @throws ReportPortalException if the datacenter name is invalid
   */
  public static DataCenter resolveDatacenterName(String oldDatacenter) {
    switch (oldDatacenter) {
      case "EU":
      case "EU_CENTRAL":
        return DataCenter.EU;
      case "US":
      case "US_WEST":
        return DataCenter.US;
      case "US_EAST":
        return DataCenter.US_EAST;
      default:
        throw new ReportPortalException(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
            "Invalid SauceLabs Datacenter value");
    }

  }
}
