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

package com.epam.reportportal.saucelabs.client;

import com.epam.reportportal.saucelabs.model.IntegrationProperties;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestClientBuilder {

  private final BasicTextEncryptor textEncryptor;

  public RestClientBuilder(BasicTextEncryptor textEncryptor) {
    this.textEncryptor = textEncryptor;
  }

  public RestTemplate buildRestTemplate(IntegrationProperties sp) {

    return new RestTemplateBuilder()
        .basicAuthentication(sp.getUsername(), textEncryptor.decrypt(sp.getToken()))
        .rootUri(sp.getDatacenter().getBaseUrl())
        .build();
  }
}
