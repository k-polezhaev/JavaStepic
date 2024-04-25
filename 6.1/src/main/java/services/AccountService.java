package services;

import models.AccountServiceImpl;
import models.UserProfile;
import db.IDBService;
import db.DBException;

public class AccountService implements AccountServiceImpl {
    private final IDBService dbService;

    public AccountService(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void singUp(String login, String password) {
        try {
            dbService.addUser(new UserProfile(login, password));
        } catch (DBException e) {
            System.out.println("Can't sing in: " + e.getMessage());
        }
    }

    @Override
    public boolean singIn(String login, String password) {
        try {
            UserProfile profile = dbService.getUser(login);
            return profile != null && profile.getPassword().equals(password);
        } catch (DBException e) {
            System.out.println("Can't sing in: " + e.getMessage());
            return false;
        }
    }
}
