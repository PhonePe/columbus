/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.storage;

import com.phonepe.columbus.command.Executor;
import lombok.NonNull;

/**
 * A storage client is responsible for all interactions with storage.
 * <p>
 * The different operations supported by storage are modelled as
 * {@link com.phonepe.columbus.command.Command Command}s, requiring this class to be an {@link Executor}.
 */
public interface StorageClient extends Executor {

    /**
     * Each storage implementation is associated with a unique type.
     *
     * @return the type of the storage.
     */
    @NonNull
    String type();

    /**
     * Each storage instance is identified by a unique name.
     *
     * @return the name of the storage.
     */
    @NonNull
    String name();
}
