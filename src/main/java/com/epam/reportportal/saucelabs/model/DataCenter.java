package com.epam.reportportal.saucelabs.model;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataCenter {
  US_WEST("https://api.us-west-1.saucelabs.com"),
  US("https://api.us-west-1.saucelabs.com"),

  EU("https://eds.eu-central-1.saucelabs.com"),
  EU_CENTRAL("https://eds.eu-central-1.saucelabs.com"),

  US_EAST("https://eds.us-east-1.saucelabs.com");

  private final String baseUrl;

  public static DataCenter fromString(String dataCenter) {
    return Arrays.stream(DataCenter.values())
        .filter(dc -> dc.name().equalsIgnoreCase(dataCenter))
        .findFirst()
        .orElse(US);
  }
}
