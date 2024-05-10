package ru.nicshal.http.server.utils;

public class ApplicationConst {

    public static final String PATH_NAME = "./static";
    public static final int PORT_NUMBER = 8189;
    public static final int THREAD_COUNT = 10;
    public static final String REPOSITORY_TYPE = "Postgres";

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/nicshal";
    public static final String DATABASE_USER = "nicshal";
    public static final String DATABASE_PASSWORD = "nic25041970#";
    public static final String ADD_PRODUCT = """
            insert into education.products(id, title, price)
            values (?, ?, ?)
            """;
    public static final String UPDATE_PRODUCT = """
            update education.products
            set title = ?,
                price = ?
            where id = ?
            """;
    public static final String DELETE_PRODUCT = """
            delete from education.products
            where id = ?
            """;
    public static final String GET_ALL_PRODUCTS = """
            select id, title, price
            from education.products
            """;
    public static final String GET_PRODUCT_BY_ID = """
            select id, title, price
            from education.products
            where id = ?
            """;

    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_PRICE = "price";

    public static final String DATABASE_ERROR = "Ошибка при обращении к БД. Обратитесь в техподдержку";

    private ApplicationConst() {}
}