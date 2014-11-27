/*
 * Copyright (c) 2014. Oxborrow.nl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.oxborrow.spark.security.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Date: 27-11-14
 *
 * @author oscar
 */
public class PropertiesModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesModule.class);
    private static final String SPARKSECURITY_PROPERTIES = "sparksecurity.properties";
    private static final String DEFAULT_SPARKSECURITY_PROPERTIES = "default_sparksecurity.properties";

    @Override
    protected void configure() {
        Properties properties = null;
            properties = loadProperties(SPARKSECURITY_PROPERTIES);

        Names.bindProperties(binder(), properties);
    }

    private Properties loadProperties(String propertiesFile) {
        Properties properties = new Properties();
        try {
            URL resource = getClass().getClassLoader().getResource(propertiesFile);
            if (resource != null) {
                properties.load(resource.openStream());
            } else {
                return loadProperties(DEFAULT_SPARKSECURITY_PROPERTIES);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to bind properties.", e);
            throw new RuntimeException(e);
        }
        return properties;
    }


}
