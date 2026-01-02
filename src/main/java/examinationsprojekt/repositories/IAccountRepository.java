package examinationsprojekt.repositories;

import examinationsprojekt.models.Account;
import examinationsprojekt.models.User;

import java.util.List;

public interface IAccountRepository {
    void save(Account createdAccount) throws Exception;

    Account findSingleAccount(String accountName) throws Exception;

    List<Account> findAllUserAccounts() throws Exception;

    boolean delete(Account deletedAccount) throws Exception;
}
