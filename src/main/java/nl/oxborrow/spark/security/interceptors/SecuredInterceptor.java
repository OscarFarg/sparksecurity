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

package nl.oxborrow.spark.security.interceptors;

import nl.oxborrow.spark.security.annotations.Secured;
import nl.oxborrow.spark.security.auth.Permission;
import nl.oxborrow.spark.security.context.SecurityContext;
import nl.oxborrow.spark.security.context.SecurityContextProvider;
import nl.oxborrow.spark.security.exceptions.ForbiddenException;
import nl.oxborrow.spark.security.exceptions.UnauthorizedException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

/**
 * Date: 24-11-14
 *
 * @author oscar
 */
public class SecuredInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        SecurityContext context = SecurityContextProvider.getContext();

        //Check if the user is authenticated
        if (!context.isAuthenticated()) {
            throw new ForbiddenException();
        }

        Secured annotation = methodInvocation.getMethod().getAnnotation(Secured.class);
        if (!ArrayUtils.isEmpty(annotation.permissions())) {

            for (String permissionValue : annotation.permissions()) {
                Collection<? extends Permission> permissions = context.getAuthentication().getPermissions();
                if (permissions == null || !hasPermission(permissions, permissionValue)) {
                    throw new UnauthorizedException();
                }

            }

        }
        SecurityContextProvider.clearContext();

        return methodInvocation.proceed();
    }


    private boolean hasPermission(Collection<? extends Permission> permissions, String permissionValue) {
        boolean permissionExists = false;

        for (Permission permission : permissions) {
            if (permission.isPermission(permissionValue)) {
                permissionExists = true;
            }
        }
        return permissionExists;
    }


}
