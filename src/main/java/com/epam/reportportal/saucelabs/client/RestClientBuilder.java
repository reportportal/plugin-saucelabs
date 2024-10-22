package com.epam.reportportal.saucelabs.client;

import com.epam.reportportal.saucelabs.model.SauceProperties;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestClientBuilder {

  private final BasicTextEncryptor textEncryptor;

  public RestClientBuilder(BasicTextEncryptor textEncryptor) {
    this.textEncryptor = textEncryptor;
  }

  public RestTemplate buildRestTemplate(SauceProperties sp) {

    return new RestTemplateBuilder()
        .basicAuthentication(sp.getUsername(), textEncryptor.decrypt(sp.getToken()))
        .rootUri(sp.getDatacenter().getBaseUrl())
        .build();

  }
}
