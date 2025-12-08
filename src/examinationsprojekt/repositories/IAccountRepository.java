package examinationsprojekt.repositories;

import examinationsprojekt.models.Account;

import java.util.List;

public interface IAccountRepository {
    boolean save(Account createdAccount);

    List<Account> findAll();

    boolean update(Account updatedAccount);

    boolean delete(Account deletedAccount);
}
