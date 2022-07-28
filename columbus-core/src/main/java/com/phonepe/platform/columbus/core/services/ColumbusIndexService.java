package com.phonepe.platform.columbus.core.services;

import com.phonepe.platform.columbus.models.core.request.ColumbusCoreServiceRequest;

public interface ColumbusIndexService<
    I extends ColumbusCoreServiceRequest,
    U extends ColumbusCoreServiceRequest,
    R> {
    void initialise(I initialiseRequest);

    R updateMapping(U updateRequest);
}
