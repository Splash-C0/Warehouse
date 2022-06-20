/*
 * Copyright (c) 2022. , Osama Raddad
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dsl.api.dependency

import core.dependency.entity.CreationPattern.*
import core.dependency.entity.Factory
import core.warehouse.entity.Warehouse

@PublishedApi
internal const val LOCK = "Lock"

@PublishedApi
internal inline fun <reified T> Factory.resolveInstance(warehouse: Warehouse): T {
    val value = when (creationPattern) {
        NEW -> constructor(warehouse)
        SINGLETON -> {
            synchronized(LOCK) {
                if (instance == null) instance = constructor(warehouse)
                instance
            }
        }
        REUSABLE -> {
            if (instance == null) instance = constructor(warehouse)
            instance
        }
    }
    return safeCast(value)
}
