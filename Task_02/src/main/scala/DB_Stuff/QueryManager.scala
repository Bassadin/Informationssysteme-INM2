package DB_Stuff

import DB_Stuff.RedisInsertionHandlers._
import JsonDefinitions.ArticleProtocol.articleFormat
import JsonDefinitions.AuthorProtocol.{LongJsonFormat, authorFormat, listFormat}
import JsonDefinitions.{Article, Author}
import spray.json._

import java.util

object QueryManager {
    def titleByID(articleID: Long): String = {
        val articleJson: String =
            RedisDatabaseManagerReadMode.jedisInstance.get(ArticleManager.redisPrefix + articleID);
        articleJson.parseJson.convertTo[Article].title;
    };

    def authors(articleID: Long): List[Author] = {
        val authorIDListForArticleJson: String = RedisDatabaseManagerReadMode.jedisInstance.get(
          ArticleToAuthorRelationManager.redisPrefix + articleID
        );

        val authorIDList: List[Long] = authorIDListForArticleJson.parseJson.convertTo[List[Long]];

        val authorList: List[Author] = authorIDList.map(authorID => {
            val redisJsonString: String = RedisDatabaseManagerReadMode.jedisInstance
                .get(AuthorManager.redisPrefix + authorID);

            redisJsonString.parseJson.convertTo[Author];
        })

        authorList;
    };
    def articles(authorID: Long): List[Article] = {
        val articleIDListForAuthorJson: util.Set[String] = RedisDatabaseManagerReadMode.jedisInstance.smembers(
          AuthorToArticleRelationManager.redisPrefix + authorID.toString
        );

        val articleList: List[Article] = articleIDListForAuthorJson
            .toArray()
            .map(eachArticleIDString => {

                val redisArticleJsonString: String = RedisDatabaseManagerReadMode.jedisInstance
                    .get(ArticleManager.redisPrefix + eachArticleIDString);

                redisArticleJsonString.parseJson.convertTo[Article];
            })
            .toList;

        articleList;
    };
    def referencedBy(articleID: Long): List[Article] = {
        val referencedArticleIDListForReferencingArticleJson: util.Set[String] =
            RedisDatabaseManagerReadMode.jedisInstance.smembers(
              ReferencedArticleToReferencingArticleRelationManager.redisPrefix + articleID.toString
            );

        val referencedByArticleList: List[Article] = referencedArticleIDListForReferencingArticleJson
            .toArray()
            .map(eachReferencedByArticleIDString => {
                val redisArticleJsonString: String = RedisDatabaseManagerReadMode.jedisInstance
                    .get(ArticleManager.redisPrefix + eachReferencedByArticleIDString);

                redisArticleJsonString.parseJson.convertTo[Article];
            })
            .toList;

        referencedByArticleList;
    };

    def mostArticles(): List[Author] = {
        val authorsWithMostArticlesIDs: util.List[String] = RedisDatabaseManagerReadMode.jedisInstance.zrange(
          AuthorToArticleRelationManager.AUTHOR_ARTICLE_AMOUNTS_SORTED_SET_KEY,
          -1,
          -1
        );

        val authorList = authorsWithMostArticlesIDs.toArray.map(authorIDString => {
            val redisJsonString: String = RedisDatabaseManagerReadMode.jedisInstance
                .get(AuthorManager.redisPrefix + authorIDString);

            redisJsonString.parseJson.convertTo[Author];
        });

        authorList.toList
    };

    def distinctAuthorsExact(): Long = {
        RedisDatabaseManagerReadMode.jedisInstance.scard(AuthorManager.AUTHORS_IDS_EXACT_SET_KEY);
    };
    def distinctAuthorsHyperLogLog(): Long = {
        RedisDatabaseManagerReadMode.jedisInstance.pfcount(AuthorManager.AUTHORS_IDS_PF_SET_KEY);
    };
}
