package net.hasanguner.service;

import net.hasanguner.model.request.TransactionQueryRequest;
import net.hasanguner.model.request.TransactionsReportRequest;
import net.hasanguner.model.response.TransactionQueryResponse;
import net.hasanguner.model.response.TransactionsReportResponse;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by hasanguner on 06/11/2016.
 */
public interface TransactionService {

    Future<Optional<TransactionsReportResponse>> reportTransactions(TransactionsReportRequest request, String authorizationToken);

    Future<Optional<TransactionQueryResponse>> queryTransactions(TransactionQueryRequest request, String authorizationToken);

}
