package ru.job4j.articles.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.articles.model.Article;
import ru.job4j.articles.model.Word;
import ru.job4j.articles.service.generator.ArticleGenerator;
import ru.job4j.articles.store.Store;

import java.util.LinkedList;
import java.util.List;

public class SimpleArticleService implements ArticleService {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleArticleService.class.getSimpleName());
    private final ArticleGenerator articleGenerator;

    public SimpleArticleService(ArticleGenerator articleGenerator) {
        this.articleGenerator = articleGenerator;
    }

    @Override
    public void generate(Store<Word> wordStore, int count, Store<Article> articleStore) {
        LOGGER.info("Геренация статей в количестве {}", count);
        List<Word> words = wordStore.findAll();
        List<Article> articleList = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            LOGGER.info("Сгенерирована статья № {}", i);
            articleList.add(articleGenerator.generate(words));
            if (articleList.size() == 10000) {
                articleList.forEach(articleStore::save);
                articleList.clear();
            }
        }
    }
}