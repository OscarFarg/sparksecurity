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

package nl.oxborrow.spark.security.context;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * Date: 25-11-14
 *
 * @author oscar
 */
public class SecurityContextProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextProvider.class);

    private static SecurityContextHolderStrategy STRATEGY;
    private static final String SYSTEM_PROPERTY = "spark.security.strategy";
    private static String strategyClassName = System.getProperty(SYSTEM_PROPERTY);

    static {
        init();
    }

    private static void init() {
        if (StringUtils.isBlank(strategyClassName)) {
            STRATEGY = new DefaultSecurityContextHolderStrategy();
        } else {
            try {
                Class<?> clazz = Class.forName(strategyClassName);
                Constructor<?> constructor = clazz.getConstructor();
                STRATEGY = (SecurityContextHolderStrategy) constructor.newInstance();
            } catch (Exception e) {
               LOGGER.error("Failed to retrieve the SecurityContextHolderStrategy", e);
            }
        }
    }

    public static void clearContext() {
        STRATEGY.clear();
    }

    public static SecurityContext getContext() {
        return STRATEGY.getContext();
    }

    public static void setContext(SecurityContext context) {
        STRATEGY.setContext(context);
    }

    public static SecurityContext newContext() {
        return STRATEGY.newContext();
    }

}
