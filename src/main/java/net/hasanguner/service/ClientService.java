package net.hasanguner.service;

import net.hasanguner.model.request.ClientRequest;
import net.hasanguner.model.response.ClientResponse;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by hasanguner on 06/11/2016.
 */
public interface ClientService {

    Future<Optional<ClientResponse>> getClientDetails(ClientRequest request, String authorizationToken);

}
