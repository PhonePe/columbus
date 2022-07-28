package com.phonepe.platform.columbus.core.services;

import com.phonepe.platform.columbus.models.core.request.ColumbusCoreServiceRequest;

import java.util.List;

public interface ColumbusEntityService<
    C extends ColumbusCoreServiceRequest,
    S extends ColumbusCoreServiceRequest,
    U extends ColumbusCoreServiceRequest,
    D extends ColumbusCoreServiceRequest,
    R> {
    void create(C createRequest);

    R get(S getRequest);

    List<R> search(S searchRequest);

    void update(U updateRequest);

    void delete(D deleteRequest);
}