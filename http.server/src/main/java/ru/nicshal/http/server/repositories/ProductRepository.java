package ru.nicshal.http.server.repositories;

import ru.nicshal.http.server.data.Item;

import java.util.List;
import java.util.UUID;

public interface ProductRepository {

    void init();

    List<Item> getItems();

    List<Item> getItemByID(UUID id);

    void save(Item item);

    boolean update(Item item);

    boolean delete(UUID id);

}