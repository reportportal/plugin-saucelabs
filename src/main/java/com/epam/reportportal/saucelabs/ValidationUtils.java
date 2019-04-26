package com.epam.reportportal.saucelabs;

import com.epam.ta.reportportal.commons.Predicates;
import com.epam.ta.reportportal.ws.model.ErrorType;

import java.util.Map;

import static com.epam.reportportal.saucelabs.SaucelabsExtension.DATA_CENTER;
import static com.epam.reportportal.saucelabs.SaucelabsExtension.JOB_ID;
import static com.epam.ta.reportportal.commons.validation.BusinessRule.expect;

/**
 * @author <a href="mailto:pavel_bortnik@epam.com">Pavel Bortnik</a>
 */
public class ValidationUtils {

	public static void validateParams(Map params) {
		expect(params.get(DATA_CENTER), Predicates.notNull()).verify(ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
				DATA_CENTER + " parameter should be provided"
		);
		expect(params.get(JOB_ID), Predicates.notNull()).verify(
				ErrorType.UNABLE_INTERACT_WITH_INTEGRATION,
				JOB_ID + " parameter should be provided"
		);
	}

}
