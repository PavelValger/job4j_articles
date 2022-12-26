package ru.job4j.articles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.service.SimpleArticleService;
import ru.job4j.articles.service.generator.RandomArticleGenerator;
import ru.job4j.articles.store.ArticleStore;
import ru.job4j.articles.store.WordStore;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Application {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Application.class.getSimpleName());
    private static final int TARGET_COUNT = 1_000_000;

    public static void main(String[] args) {
        Properties properties = loadProperties();
        try (WordStore wordStore = new WordStore(properties);
             ArticleStore articleStore = new ArticleStore(properties)) {
            new SimpleArticleService(new RandomArticleGenerator())
                    .generate(wordStore, TARGET_COUNT, articleStore);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties() {
        LOGGER.info("Загрузка настроек приложения");
        Properties properties = new Properties();
        try (InputStream in = Application.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(in);
        } catch (IOException e) {
            LOGGER.error("Не удалось загрузить настройки. { }", e.getCause());
            throw new IllegalStateException();
        }
        return properties;
    }
}