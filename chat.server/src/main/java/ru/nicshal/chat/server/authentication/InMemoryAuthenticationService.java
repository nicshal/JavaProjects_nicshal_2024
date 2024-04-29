package ru.nicshal.chat.server.authentication;

import java.util.ArrayList;
import java.util.List;

public class InMemoryAuthenticationService implements AuthenticationService {

    private class User {

        private final String login;
        private final String password;
        private final String nickname;
        private final String group;

        public User(String login, String password, String nickname) {
            this(login, password, nickname, "user");
        }

        public User(String login, String password, String nickname, String group) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
            this.group = group;
        }

    }

    private final List<User> users;

    public InMemoryAuthenticationService() {
        this.users = new ArrayList<>();
        this.users.add(new User("admin", "12345", "admin", "admin"));
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (User u : users) {
            if (u.login.equals(login) && u.password.equals(password)) {
                return u.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean register(String login, String password, String nickname) {
        if (isLoginAlreadyExist(login)) {
            return false;
        }
        if (isNicknameAlreadyExist(nickname)) {
            return false;
        }
        users.add(new User(login, password, nickname));
        return true;
    }

    @Override
    public boolean isLoginAlreadyExist(String login) {
        for (User u : users) {
            if (u.login.equals(login)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isNicknameAlreadyExist(String nickname) {
        for (User u : users) {
            if (u.nickname.equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAdmin(String nickname) {
        for (User u : users) {
            if (u.nickname.equals(nickname)) {
                return "admin".equals(u.group);
            }
        }
        return false;
    }

}