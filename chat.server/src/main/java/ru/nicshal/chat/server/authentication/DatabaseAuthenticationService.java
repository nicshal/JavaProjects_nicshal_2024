package ru.nicshal.chat.server.authentication;

import ru.nicshal.chat.server.repositories.PostgresRepository;

public class DatabaseAuthenticationService implements AuthenticationService {

    PostgresRepository postgresRepository;

    public DatabaseAuthenticationService() {
        postgresRepository = new PostgresRepository();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return postgresRepository.getNicknameByLoginAndPassword(login, password);
    }

    @Override
    public boolean register(String login, String password, String nickname) {
        return postgresRepository.addUser(login, password, nickname);
        //return true;
    }

    @Override
    public boolean isLoginAlreadyExist(String login) {
        return postgresRepository.isLoginAlreadyExist(login);
    }

    @Override
    public boolean isNicknameAlreadyExist(String nickname) {
        return postgresRepository.isNicknameAlreadyExist(nickname);
    }

    @Override
    public boolean isAdmin(String nickname) {
        return postgresRepository.isAdmin(nickname);
    }

}