package db;

import models.UserProfile;

public interface IDBService {
    long addUser(UserProfile userProfile) throws DBException;
    UserProfile getUser(String login) throws DBException;
    void create() throws DBException;
    void cleanUp() throws DBException;
    void check() throws DBException;
}
