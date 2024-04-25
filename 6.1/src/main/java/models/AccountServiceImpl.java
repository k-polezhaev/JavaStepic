package models;

public interface AccountServiceImpl {
    void singUp(String login, String password);
    boolean singIn(String login, String password);
}
