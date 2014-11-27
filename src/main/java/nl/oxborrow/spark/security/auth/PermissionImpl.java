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

package nl.oxborrow.spark.security.auth;

/**
 * Date: 25-11-14
 *
 * @author oscar
 */
public class PermissionImpl implements Permission {

    private String permission;

    public PermissionImpl(String permission) {
        this.permission = permission;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public boolean isPermission(String permission) {
        return permission.equals(this.permission);
    }
}
