package ru.nicshal.chat.server.repositories;

import java.sql.*;

public class PostgresRepository {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/nicshal";
    private static final String DATABASE_USER = "nicshal";
    private static final String DATABASE_PASSWORD = "***";
    private static final String GET_NICKNAME_BY_LOGIN_PASSWORD = """
            select ur.nickname
            from education.users ur
            where ur.login = ? and ur.password = ?
            """;
    private static final String CHECK_LOGIN = """
            select ur.login
            from education.users ur
            where ur.login = ?
            """;
    private static final String CHECK_NICKNAME = """
            select ur.nickname
            from education.users ur
            where ur.nickname = ?
            """;
    private static final String CHECK_ADMIN = """
            select ur.nickname
            from education.users ur
            where ur.nickname = ? and ur.role = 'admin'
            """;
    private static final String ADD_USER = """
            insert into education.users(login, password, nickname, role)
            values (?, ?, ?, ?)
            """;

    public String getNicknameByLoginAndPassword(String login, String password) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_NICKNAME_BY_LOGIN_PASSWORD)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                try (ResultSet userResultSet = preparedStatement.executeQuery()) {
                    if (userResultSet.next()) {
                        return userResultSet.getString("nickname");
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к БД. Обратитесь в техподдержку", e);
        }
    }

    public boolean isLoginAlreadyExist(String login) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LOGIN)) {
                preparedStatement.setString(1, login);
                try (ResultSet userResultSet = preparedStatement.executeQuery()) {
                    return userResultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к БД. Обратитесь в техподдержку", e);
        }
    }

    public boolean isNicknameAlreadyExist(String nickname) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_NICKNAME)) {
                preparedStatement.setString(1, nickname);
                try (ResultSet userResultSet = preparedStatement.executeQuery()) {
                    return userResultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к БД. Обратитесь в техподдержку", e);
        }
    }

    public boolean isAdmin(String nickname) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ADMIN)) {
                preparedStatement.setString(1, nickname);
                try (ResultSet userResultSet = preparedStatement.executeQuery()) {
                    return userResultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к БД. Обратитесь в техподдержку", e);
        }
    }

    public boolean addUser(String login, String password, String nickname) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER)) {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, nickname);
                preparedStatement.setString(4, "user");
                return preparedStatement.executeUpdate() != 0;

            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обращении к БД. Обратитесь в техподдержку", e);
        }
    }

}