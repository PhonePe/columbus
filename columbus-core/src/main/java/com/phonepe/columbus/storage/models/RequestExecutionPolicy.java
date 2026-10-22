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
package com.phonepe.columbus.storage.models;

/**
 * A class to represent the different ways a storage might execute a time-consuming request.
 * <p>
 * The request execution could be synchronous, asynchronous, or periodically managed.
 */
public enum RequestExecutionPolicy {
    /**
     * Submit the request and return without waiting for completion.
     */
    SUBMIT,
    /**
     * Submit the request and wait for completion. For periodic functions, this would wait until the next trigger and
     * completion.
     */
    WAIT_FOR_COMPLETION,
    /**
     * Submit the request and wait for completion, triggering it immediately in case of periodic functions.
     */
    FORCE_COMPLETION,
}
