import JsonDefinitions.{Article, Author}
import redis.clients.jedis.{Jedis, JedisPool, JedisPooled}
import redis.clients.jedis.args.FlushMode

object RedisDatabaseManager {
    val DB_HOST = "localhost";
    val DB_PORT = 32321;

    val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);
    val jedisInstance: Jedis = jedisConnectionPool.getResource;

    jedisInstance.flushAll(FlushMode.SYNC);

    final val articleToArticleIndexAutoIncrementKey = "ai_index_article_to_article_relation";

    /** Add to the DB multiple articles that are being referenced by another article.
      * @param referencingArticle
      *   The article that's doing the referencing.
      * @param referencedArticlesIDs
      *   The articles that are being referenced in form of IDs.
      */
    def addArticleToArticlesRelation(referencingArticle: Article, referencedArticlesIDs: List[Long]): Unit = {
        referencedArticlesIDs.foreach(eachReferencedArticleID => {
            jedisInstance.incr(articleToArticleIndexAutoIncrementKey);
            val currentAutoIncrementId = jedisInstance.get(articleToArticleIndexAutoIncrementKey)
            val articleToArticleRelationRedisSetPrefixName: String =
                s"article_to_article_relation_$currentAutoIncrementId";

            jedisInstance.hset(
              articleToArticleRelationRedisSetPrefixName,
              "referencing_article_id",
              referencingArticle.id.toString
            );
            jedisInstance.hset(
              articleToArticleRelationRedisSetPrefixName,
              "referenced_article_id",
              eachReferencedArticleID.toString
            );
        })
    }

    final val authorArticleIndexAutoIncrementKey = "ai_index_author_to_article_relation";

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
        authors.foreach(eachAuthor => {
            jedisInstance.incr(authorArticleIndexAutoIncrementKey);
            val currentAutoIncrementId = jedisInstance.get(authorArticleIndexAutoIncrementKey)
            val articleAuthorRelationRedisSetPrefixName: String = s"author_article_relation_$currentAutoIncrementId";

            jedisInstance.hset(articleAuthorRelationRedisSetPrefixName, "article_id", article.id.toString);
            jedisInstance.hset(articleAuthorRelationRedisSetPrefixName, "author_id", eachAuthor.id.toString);
        });
    }

    /** Add multiple authors to the DB
      * @param authorsToAdd
      *   The list of authors to add.
      */
    def addAuthors(authorsToAdd: List[Author]): Unit = {
        authorsToAdd.foreach(eachAuthor => {
            val authorRedisSetPrefixName: String = s"author_${eachAuthor.id}";

            // Return out if author key already exists
            if (jedisInstance.keys(authorRedisSetPrefixName).size() > 0) {
                return;
            }

            jedisInstance.hset(authorRedisSetPrefixName, "title", eachAuthor.name);

            eachAuthor.org match {
                case Some(i) => jedisInstance.hset(authorRedisSetPrefixName, "org", i);
                case None    =>
            }

        });
    }

    /** Add a single article to the DB
      * @param articleToAdd
      *   The Article to add to the DB.
      */
    def addArticle(articleToAdd: Article): Unit = {
        val articleRedisSetPrefixName: String = s"article_${articleToAdd.id}";
        jedisInstance.hset(articleRedisSetPrefixName, "title", articleToAdd.title);
        jedisInstance.hset(articleRedisSetPrefixName, "year", articleToAdd.year.toString);
        jedisInstance.hset(articleRedisSetPrefixName, "n_citation", articleToAdd.n_citation.toString);
        jedisInstance.hset(articleRedisSetPrefixName, "page_start", articleToAdd.page_start);
        jedisInstance.hset(articleRedisSetPrefixName, "page_start", articleToAdd.page_start);

        articleToAdd.doc_type match {
            case Some(i) => jedisInstance.hset(articleRedisSetPrefixName, "doc_type", i);
            case None    =>
        }

        jedisInstance.hset(articleRedisSetPrefixName, "publisher", articleToAdd.publisher);
        jedisInstance.hset(articleRedisSetPrefixName, "volume", articleToAdd.volume);
        jedisInstance.hset(articleRedisSetPrefixName, "issue", articleToAdd.issue);

        articleToAdd.DOI match {
            case Some(i) => jedisInstance.hset(articleRedisSetPrefixName, "DOI", i);
            case None    =>
        }
    }
}
