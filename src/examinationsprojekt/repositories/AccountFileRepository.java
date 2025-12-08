package examinationsprojekt.repositories;

import examinationsprojekt.models.Account;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class AccountFileRepository implements IAccountRepository {
    public boolean save(Account createdAccount) {
        try {
            File folder = new File("saveData");
            if (!folder.exists()) {
                folder.mkdir();
            }

            FileOutputStream saveData = new FileOutputStream(new File(folder, createdAccount.getName()));
            ObjectOutputStream objectOut = new ObjectOutputStream(saveData);
            objectOut.writeObject(createdAccount);
            objectOut.close();
            saveData.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return true;
    }

    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        try {
            File folder = new File("saveData");

            if (!folder.exists() || folder.listFiles() == null) {
                return accounts;
            }

            for (File file : folder.listFiles()) {
                FileInputStream saveData = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(saveData);
                accounts.add((Account) objectIn.readObject());
                objectIn.close();
                saveData.close();
            }
        } catch (IOException | NullPointerException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return accounts;
    }

    public boolean update(Account updatedAccount) {
        try {
            File folder = new File("saveData");
            for (File file : folder.listFiles()) {
                if (file.getName().equals(updatedAccount.getName())) {
                    FileOutputStream saveData = new FileOutputStream(new File(folder, updatedAccount.getName()));
                    ObjectOutputStream objectOut = new ObjectOutputStream(saveData);
                    objectOut.writeObject(updatedAccount);
                    objectOut.close();
                    saveData.close();
                }
            }
        } catch (IOException | NullPointerException exception) {
            System.out.println("No files found on check.");
            exception.printStackTrace();
        }

        return true;
    }

    public boolean delete(Account deletedAccount) {
        try {
            File folder = new File("saveData");
            for (File file : folder.listFiles()) {
                if (file.getName().equals(deletedAccount.getName())) {
                    file.delete();
                }
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }

        return true;
    }
}
