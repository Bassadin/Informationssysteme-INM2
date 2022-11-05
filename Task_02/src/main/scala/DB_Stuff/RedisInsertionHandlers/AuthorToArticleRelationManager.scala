package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.{Article, Author}

object AuthorToArticleRelationManager extends RedisManagerTrait {
    final val redisPrefix = "r_auth-article_";

    final val AUTHOR_ARTICLE_AMOUNTS_SORTED_SET_KEY = redisPrefix + "amounts";

    /** Add to the DB a relation from an article to multiple authors.
      *
      * @param article
      *   The article to add the authors to.
      * @param authors
      *   The authors to add to the relation.
      */
    def addRelation(
        authors: List[Author],
        article: Article
    ): Unit = {
        authors.foreach(eachAuthor => {
            val authorToArticlesRelationRedisSetName: String = redisPrefix + eachAuthor.id;
            RedisDatabaseManagerWriteMode.jedisPipeline
                .sadd(authorToArticlesRelationRedisSetName, article.id.toString);

            RedisDatabaseManagerWriteMode.jedisPipeline
                .zincrby(AUTHOR_ARTICLE_AMOUNTS_SORTED_SET_KEY, 1, eachAuthor.id.toString);
        })
    }
}
