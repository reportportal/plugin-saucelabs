package com.epam.reportportal.saucelabs.model;

import static com.epam.reportportal.saucelabs.model.SaucelabsProperties.ACCESS_TOKEN;
import static com.epam.reportportal.saucelabs.model.SaucelabsProperties.DATA_CENTER;
import static com.epam.reportportal.saucelabs.model.SaucelabsProperties.USERNAME;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SauceProperties {

  private final String username;
  private final String token;
  private final DataCenter datacenter;

  private String jobId;

  public SauceProperties(String username, String token, DataCenter datacenter) {
    this.username = username;
    this.token = token;
    this.datacenter = datacenter;
  }

  public SauceProperties(Map<String, Object> params) {
    this.username = (String) params.get(USERNAME.getName());
    this.token = (String) params.get(ACCESS_TOKEN.getName());
    this.datacenter = DataCenter.fromString((String) params.get(DATA_CENTER.getName()));
  }
}
