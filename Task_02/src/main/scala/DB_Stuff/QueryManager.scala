package DB_Stuff

import JsonDefinitions.ArticleProtocol.articleFormat
import JsonDefinitions.{Article, Author}
import spray.json._

object QueryManager {
    // TODO
    def titleByID(articleID: Long): String = {
        val articleJson: String = RedisDatabaseManagerReadMode.jedisInstance.get(s"article_$articleID");

        articleJson.parseJson.convertTo[Article].title;
    };
    def authors(articleID: Long): List[Author] = ???;
    def articles(authorID: Long): List[Article] = ???;
    def referencedBy(articleID: Long): List[Article] = ???;

    // TODO
    def mostArticles(): List[Author] = ???;
    def distinctAuthorsExact(): Long = ???;
    def distinctAuthorsHyperLogLog(): Long = ???;
}
