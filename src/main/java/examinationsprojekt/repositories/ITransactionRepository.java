package examinationsprojekt.repositories;


import examinationsprojekt.models.Transaction;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ITransactionRepository {
    void save(Transaction createdTransaction) throws SQLException;

    Transaction findSingleTransaction(UUID transactionId) throws Exception;

    List<Transaction> findAllAccountTransactions() throws Exception;

    boolean delete(Transaction deletedTransaction) throws Exception;
}
