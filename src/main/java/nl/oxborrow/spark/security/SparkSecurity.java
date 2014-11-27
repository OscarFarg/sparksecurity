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

package nl.oxborrow.spark.security;

import com.google.inject.Guice;
import com.google.inject.Injector;
import nl.oxborrow.spark.security.modules.AnnotationModule;
import nl.oxborrow.spark.security.exceptions.ForbiddenException;
import nl.oxborrow.spark.security.exceptions.MustacheTemplateExceptionHandler;
import nl.oxborrow.spark.security.exceptions.UnauthorizedException;
import nl.oxborrow.spark.security.filter.SecurityFilter;
import nl.oxborrow.spark.security.filter.SecurityHandler;
import nl.oxborrow.spark.security.modules.SecurityModule;
import nl.oxborrow.spark.security.modules.PropertiesModule;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

/**
 * Date: 25-11-14
 *
 * @author oscar
 */
public class SparkSecurity {

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkSecurity.class);

    private static Class<? extends SecurityHandler> securityHandlerClass;

    public static void init() {
        LOGGER.info("Initializing Spark Security");
        Validate.notNull(securityHandlerClass, "Please set a SecurityHandler.");

        Injector injector = Guice.createInjector(new SecurityModule(), new AnnotationModule(), new PropertiesModule());
        Spark.before(injector.getInstance(SecurityFilter.class));

        MustacheTemplateExceptionHandler exceptionHandler = injector.getInstance(MustacheTemplateExceptionHandler.class);
        Spark.exception(ForbiddenException.class, exceptionHandler);

        Spark.exception(UnauthorizedException.class, (exception, request, response) -> {
            response.status(UnauthorizedException.CODE);
            response.body("401 Unauthorized");
        });
    }

    public static Class<? extends SecurityHandler> getSecurityHandlerClass() {
        return securityHandlerClass;
    }

    public static void setSecurityHandlerClass(Class<? extends SecurityHandler> securityHandlerClass) {
        SparkSecurity.securityHandlerClass = securityHandlerClass;
    }
}
