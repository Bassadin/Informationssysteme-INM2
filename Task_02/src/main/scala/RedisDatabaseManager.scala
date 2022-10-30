import JsonDefinitions.{Article, Author}
import redis.clients.jedis.{Jedis, JedisPool, JedisPooled, Pipeline}
import redis.clients.jedis.args.FlushMode

object RedisDatabaseManager {
    val DB_HOST = "localhost";
    val DB_PORT = 32321;

    val jedisConnectionPool: JedisPool = new JedisPool(DB_HOST, DB_PORT);
    val jedisInstance: Jedis = jedisConnectionPool.getResource;

    jedisInstance.flushAll(FlushMode.SYNC);

    def closeConnection() : Unit = {
        jedisConnectionPool.close();
    }

    final val articleToArticleIndexAutoIncrementKey = "ai_index_relation_article_to_article";

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
                s"relation_article_to_article_$currentAutoIncrementId";

            val addArticleToArticleRelationPipeline = jedisInstance.pipelined();

            addArticleToArticleRelationPipeline.hset(
              articleToArticleRelationRedisSetPrefixName,
              "referencing_article_id",
              referencingArticle.id.toString
            );
            addArticleToArticleRelationPipeline.hset(
              articleToArticleRelationRedisSetPrefixName,
              "referenced_article_id",
              eachReferencedArticleID.toString
            );

            addArticleToArticleRelationPipeline.sync();
        })
    }

    final val authorArticleIndexAutoIncrementKey = "ai_index_relation_author_to_article";

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
            val articleAuthorRelationRedisSetPrefixName: String = s"relation_author_article_$currentAutoIncrementId";

            val addArticleToAuthorPipeline = jedisInstance.pipelined();

            addArticleToAuthorPipeline.hset(articleAuthorRelationRedisSetPrefixName, "article_id", article.id.toString);
            addArticleToAuthorPipeline.hset(
              articleAuthorRelationRedisSetPrefixName,
              "author_id",
              eachAuthor.id.toString
            );

            addArticleToAuthorPipeline.sync();
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

            val addAuthorsPipeline = jedisInstance.pipelined();

            addAuthorsPipeline.hset(authorRedisSetPrefixName, "title", eachAuthor.name);

            eachAuthor.org match {
                case Some(i) => addAuthorsPipeline.hset(authorRedisSetPrefixName, "org", i);
                case None    =>
            }

            addAuthorsPipeline.sync();
        });
    }

    /** Add a single article to the DB
      * @param articleToAdd
      *   The Article to add to the DB.
      */
    def addArticle(articleToAdd: Article): Unit = {

        val addArticlePipeline = jedisInstance.pipelined();

        val articleRedisSetPrefixName: String = s"article_${articleToAdd.id}";
        addArticlePipeline.hset(articleRedisSetPrefixName, "title", articleToAdd.title);
        addArticlePipeline.hset(articleRedisSetPrefixName, "year", articleToAdd.year.toString);
        addArticlePipeline.hset(articleRedisSetPrefixName, "n_citation", articleToAdd.n_citation.toString);
        addArticlePipeline.hset(articleRedisSetPrefixName, "page_start", articleToAdd.page_start);
        addArticlePipeline.hset(articleRedisSetPrefixName, "page_start", articleToAdd.page_start);

        articleToAdd.doc_type match {
            case Some(i) => addArticlePipeline.hset(articleRedisSetPrefixName, "doc_type", i);
            case None    =>
        }

        addArticlePipeline.hset(articleRedisSetPrefixName, "publisher", articleToAdd.publisher);
        addArticlePipeline.hset(articleRedisSetPrefixName, "volume", articleToAdd.volume);
        addArticlePipeline.hset(articleRedisSetPrefixName, "issue", articleToAdd.issue);

        articleToAdd.DOI match {
            case Some(i) => addArticlePipeline.hset(articleRedisSetPrefixName, "DOI", i);
            case None    =>
        }

        addArticlePipeline.sync();
    }
}
