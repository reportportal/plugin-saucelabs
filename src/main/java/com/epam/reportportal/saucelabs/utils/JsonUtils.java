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

import com.saucelabs.saucerest.MoshiSingleton;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * Utility class for JSON operations using Moshi.
 */
public class JsonUtils {

  private JsonUtils() {
  }

  /**
   * Converts an object to its JSON representation.
   *
   * @param <T>    the type of the object
   * @param object the object to convert to JSON
   * @param clazz  the class of the object
   * @return the JSON representation of the object
   */
  public static <T> String toJson(T object, Class<?> clazz) {
    Moshi moshi = MoshiSingleton.getInstance();
    JsonAdapter<T> jsonAdapter = (JsonAdapter<T>) moshi.adapter(clazz).nonNull();
    return jsonAdapter.toJson(object);
  }
}
