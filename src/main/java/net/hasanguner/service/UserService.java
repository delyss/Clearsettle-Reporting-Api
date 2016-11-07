package net.hasanguner.service;

import net.hasanguner.model.response.AuthToken;
import net.hasanguner.model.request.Credential;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by hasanguner on 05/11/2016.
 */
public interface UserService {

    Future<Optional<AuthToken>> login(Credential credential);

}
