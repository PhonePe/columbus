package com.phonepe.platform.columbus.storage.models;

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
