package ru.nicshal.http.server.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nicshal.http.server.data.Item;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.nicshal.http.server.utils.ApplicationConst.*;

public class PostgresProductRepositoryImpl implements ProductRepository {

    private final Logger logger = LogManager.getLogger(PostgresProductRepositoryImpl.class.getName());

    @Override
    public void init() {
        logger.info("Хранилище проинициализировано");
    }

    @Override
    public List<Item> getItems() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_PRODUCTS)) {
                try (ResultSet userResultSet = preparedStatement.executeQuery()) {
                    List<Item> items = new ArrayList<>();
                    while (userResultSet.next()) {
                        Item item = new Item();
                        item.setId(UUID.fromString(userResultSet.getString(COLUMN_NAME_ID)));
                        item.setTitle(userResultSet.getString(COLUMN_NAME_TITLE));
                        item.setPrice(userResultSet.getInt(COLUMN_NAME_PRICE));
                        items.add(item);
                    }
                    return items;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_ERROR, e);
        }
    }

    @Override
    public List<Item> getItemByID(UUID id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {
                preparedStatement.setString(1, id.toString());
                try (ResultSet userResultSet = preparedStatement.executeQuery()) {
                    List<Item> items = new ArrayList<>();
                    if (userResultSet.next()) {
                        Item item = new Item();
                        item.setId(UUID.fromString(userResultSet.getString(COLUMN_NAME_ID)));
                        item.setTitle(userResultSet.getString(COLUMN_NAME_TITLE));
                        item.setPrice(userResultSet.getInt(COLUMN_NAME_PRICE));
                        items.add(item);
                    }
                    return items;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_ERROR, e);
        }
    }

    @Override
    public void save(Item item) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_PRODUCT)) {
                preparedStatement.setString(1, item.getId().toString());
                preparedStatement.setString(2, item.getTitle());
                preparedStatement.setInt(3, item.getPrice());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_ERROR, e);
        }
    }

    @Override
    public boolean update(Item item) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT)) {
                preparedStatement.setString(1, item.getTitle());
                preparedStatement.setInt(2, item.getPrice());
                preparedStatement.setString(3, item.getId().toString());
                return preparedStatement.executeUpdate() != 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_ERROR, e);
        }
    }

    @Override
    public boolean delete(UUID id) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT)) {
                preparedStatement.setString(1, id.toString());
                return preparedStatement.executeUpdate() != 0;

            }
        } catch (SQLException e) {
            throw new RuntimeException(DATABASE_ERROR, e);
        }
    }

}