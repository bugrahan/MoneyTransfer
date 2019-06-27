package com.revolut.bugrahan.api;

import com.revolut.bugrahan.model.User;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public abstract class UserApiService {
    public abstract Response createUser(User body, SecurityContext securityContext) throws NotFoundException;

    public abstract Response deleteUser(long id, SecurityContext securityContext) throws NotFoundException;

    public abstract Response getUserById(long id, SecurityContext securityContext) throws NotFoundException;

    public abstract Response updateUser(User body, long id, SecurityContext securityContext) throws NotFoundException;
}
