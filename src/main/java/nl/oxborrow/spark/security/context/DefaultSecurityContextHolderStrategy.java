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

import org.apache.commons.lang3.Validate;

/**
 * Date: 25-11-14
 *
 * @author oscar
 */
public class DefaultSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private static ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public static SecurityContext createSecurityContext() {
        return new SecurityContextImpl();
    }

    public SecurityContext getContext() {
        SecurityContext securityContext = contextHolder.get();
        if (securityContext == null) {
            securityContext = createSecurityContext();
            contextHolder.set(securityContext);
        }
        return securityContext;
    }

    public void setContext(SecurityContext securityContext) {
        Validate.notNull(securityContext, "SecurityContext cannot be null.");
        contextHolder.set(securityContext);
    }

    @Override
    public SecurityContext newContext() {
        return null;
    }

    @Override
    public void clear() {
        contextHolder.remove();
    }

}
