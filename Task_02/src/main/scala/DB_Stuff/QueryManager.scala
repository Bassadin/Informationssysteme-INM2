package DB_Stuff

import JsonDefinitions.ArticleProtocol.articleFormat
import JsonDefinitions.AuthorProtocol.{LongJsonFormat, authorFormat, listFormat}
import JsonDefinitions.{Article, Author}
import spray.json._

import java.util

object QueryManager {
    // TODO
    def titleByID(articleID: Long): String = {
        val articleJson: String =
            RedisDatabaseManagerReadMode.jedisInstance.get(ArticleManager.articleRedisPrefix + articleID);
        articleJson.parseJson.convertTo[Article].title;
    };

    def authors(articleID: Long): List[Author] = {
        val authorIDListForArticleJson: String = RedisDatabaseManagerReadMode.jedisInstance.get(
          ArticleToAuthorRelationManager.articleToAuthorRelationRedisPrefix + articleID
        );

        val authorIDList: List[Long] = authorIDListForArticleJson.parseJson.convertTo[List[Long]];

        val authorList: List[Author] = authorIDList.map(authorID => {
            val redisJsonString: String = RedisDatabaseManagerReadMode.jedisInstance
                .get(AuthorManager.authorRedisPrefix + authorID);

            redisJsonString.parseJson.convertTo[Author];
        })

        authorList;
    };
    def articles(authorID: Long): List[Article] = {
        val articleIDListForAuthorJson: util.Set[String] = RedisDatabaseManagerReadMode.jedisInstance.smembers(
          ArticleToAuthorRelationManager.authorToArticleRelationRedisPrefix + authorID.toString
        );

        val articleList: List[Article] = articleIDListForAuthorJson
            .toArray()
            .map(eachArticleIDString => {

                val redisArticleJsonString: String = RedisDatabaseManagerReadMode.jedisInstance
                    .get(ArticleManager.articleRedisPrefix + eachArticleIDString);

                redisArticleJsonString.parseJson.convertTo[Article];
            })
            .toList;

        articleList;
    };
    def referencedBy(articleID: Long): List[Article] = ???;

    // TODO
    def mostArticles(): List[Author] = ???;
    def distinctAuthorsExact(): Long = ???;
    def distinctAuthorsHyperLogLog(): Long = ???;
}
