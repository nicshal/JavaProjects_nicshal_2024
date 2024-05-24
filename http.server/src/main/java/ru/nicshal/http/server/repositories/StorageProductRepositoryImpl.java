package ru.nicshal.http.server.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nicshal.http.server.data.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StorageProductRepositoryImpl implements ProductRepository {

    private final Logger logger = LogManager.getLogger(StorageProductRepositoryImpl.class.getName());
    private List<Item> items;

    public void init() {
        logger.info("Хранилище проинициализировано");
        items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int) (Math.random() * 1000)));
        }
    }

    @Override
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Item> getItemByID(UUID id) {
        List<Item> tmp = new ArrayList<>();
        for (Item x : items) {
            if (x.getId().equals(id)) {
                tmp.add(x);
                break;
            }
        }
        return tmp;
    }

    @Override
    public void save(Item item) {
        items.add(item);
    }

    @Override
    public boolean delete(UUID id) {
        boolean result = false;
        for (Item x : items) {
            if (x.getId().equals(id)) {
                items.remove(x);
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean update(Item item) {
        boolean result = false;
        for (Item x : items) {
            if (x.getId().equals(item.getId())) {
                x.setTitle(item.getTitle());
                x.setPrice(item.getPrice());
                result = true;
            }
        }
        return result;
    }

}