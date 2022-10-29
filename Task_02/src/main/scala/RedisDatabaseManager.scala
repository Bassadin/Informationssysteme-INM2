import JsonDefinitions.{Article, Author}
import redis.clients.jedis.{Jedis, JedisPool, JedisPooled}
import redis.clients.jedis.args.FlushMode

object RedisDatabaseManager {
    val DB_HOST = "localhost";
    val DB_PORT = 32321;

    val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);
    val jedisInstance: Jedis = jedisConnectionPool.getResource;

    jedisInstance.flushAll(FlushMode.SYNC);

    /** Add to the DB multiple articles that are being referenced by another article.
      * @param referencingArticle
      *   The article that's doing the referencing.
      * @param referencedArticles
      *   The articles that are being referenced.
      */
    def addArticleToArticlesRelation(referencingArticle: Article, referencedArticles: List[Long]): Unit = {
        referencedArticles.foreach(eachReferencedArticle => {})
    }

    /** Add to the DB a relation from an article to multiple authors.
      * @param article
      *   The article to add the authors to.
      * @param authors
      *   The authors to add to the relation.
      */
    def addArticleToAuthorsRelation(
        article: Article,
        authors: List[Author]
    ): Unit = {
        authors.foreach(eachAuthor => {});
    }

    /** Add multiple authors to the DB
      * @param authorsToAdd
      *   The list of authors to add.
      */
    def addAuthors(authorsToAdd: List[Author]): Unit = {
        authorsToAdd.foreach(eachAuthor => {});
    }

    /** Add a single article to the DB
      * @param articleToAdd
      *   The Article to add to the DB.
      */
    def addArticle(articleToAdd: Article): Unit = {}
}
