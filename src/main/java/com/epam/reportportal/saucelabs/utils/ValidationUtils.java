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

package com.epam.reportportal.saucelabs.utils;

import static com.epam.reportportal.rules.commons.validation.BusinessRule.expect;
import static com.epam.reportportal.saucelabs.model.Constants.JOB_ID;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.DATA_CENTER;
import static com.epam.reportportal.saucelabs.model.IntegrationParametersNames.USERNAME;

import com.epam.reportportal.rules.exception.ErrorType;
import com.epam.ta.reportportal.commons.Predicates;
import com.epam.ta.reportportal.entity.integration.IntegrationParams;
import java.util.Map;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class ValidationUtils {

  public static final String IS_NOT_SPECIFIED = " is not specified.";

  public static void validateJobId(Map<String, Object> params) {
    expect(params.get(JOB_ID), Predicates.notNull()).verify(
        ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, JOB_ID + " parameter should be provided");
  }

  public static void validateIntegrationParams(IntegrationParams integrationParams) {

    expect(integrationParams.getParams(), Predicates.notNull()).verify(
        ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, "Integration parameters shouldn't be empty");

    Map<String, Object> params = integrationParams.getParams();
    expect(params.get(USERNAME.getName()), Predicates.notNull()).verify(
        ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, USERNAME + IS_NOT_SPECIFIED);

    expect(params.get(ACCESS_TOKEN.getName()), Predicates.notNull()).verify(
        ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, ACCESS_TOKEN + IS_NOT_SPECIFIED);

    expect(params.get(DATA_CENTER.getName()), Predicates.notNull()).verify(
        ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, DATA_CENTER + IS_NOT_SPECIFIED);
  }
}
