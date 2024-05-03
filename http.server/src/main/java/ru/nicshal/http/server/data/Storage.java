package ru.nicshal.http.server.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Storage {

    private static final Logger logger = LogManager.getLogger(Storage.class.getName());
    private static List<Item> items;

    public static void init() {
        logger.info("Хранилище проинициализировано");
        items = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int) (Math.random() * 1000)));
        }
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static void save(Item item) {
        item.setId(UUID.randomUUID());
        items.add(item);
    }

    public static boolean update(Item item) {
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