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

import static com.epam.reportportal.rules.commons.validation.BusinessRule.expect;
import static com.epam.reportportal.saucelabs.SaucelabsExtension.JOB_ID;

import com.epam.ta.reportportal.commons.Predicates;
import com.epam.reportportal.rules.exception.ErrorType;
import java.util.Map;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class ValidationUtils {

  public static void validateParams(Map params) {
    expect(params.get(JOB_ID), Predicates.notNull()).verify(
        ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, JOB_ID + " parameter should be provided");
  }

}
