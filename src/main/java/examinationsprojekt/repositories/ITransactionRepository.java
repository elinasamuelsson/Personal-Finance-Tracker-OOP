package examinationsprojekt.repositories;


import examinationsprojekt.models.Transaction;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ITransactionRepository {
    void save(Transaction createdTransaction, UUID accountId) throws SQLException;

    Optional<List<Transaction>> findAllAccountTransactions(UUID transactionId) throws Exception;

    Optional<List<Transaction>> searchTransactions(String searchPhrase) throws Exception;

    Optional<HashMap<String, Transaction>> findLatestTransactionForEachUserAccount(UUID userId) throws Exception;

    boolean delete(UUID id) throws Exception;
}
