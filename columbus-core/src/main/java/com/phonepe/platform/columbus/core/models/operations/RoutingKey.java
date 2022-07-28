package com.phonepe.platform.columbus.core.models.operations;

public interface RoutingKey {
    // if specified - need to be managed by client who specifies the same if not ES does the work.
    String getRoutingKey();
}
