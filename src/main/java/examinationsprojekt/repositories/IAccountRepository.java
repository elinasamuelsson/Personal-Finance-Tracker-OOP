package examinationsprojekt.repositories;

import examinationsprojekt.models.Account;

import java.util.List;
import java.util.UUID;

public interface IAccountRepository {
    void save(Account createdAccount) throws Exception;

    Account findSingleAccount(String accountName) throws Exception;

    List<Account> findAllUserAccounts() throws Exception;

    boolean updateAccountBalance(UUID id, double amount) throws Exception;

    boolean delete(Account deletedAccount) throws Exception;
}
