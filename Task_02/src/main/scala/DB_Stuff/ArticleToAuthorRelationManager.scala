package DB_Stuff

import DB_Stuff.RedisDatabaseManager.jedisInstance
import JsonDefinitions.{Article, Author}

object ArticleToAuthorRelationManager {
    final val authorArticleIndexAutoIncrementKey = "ai_index_relation_author_to_article";

    /** Add to the DB a relation from an article to multiple authors.
      *
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
            val articleAuthorRelationRedisSetPrefixName: String = s"relation_author_article_${eachAuthor.id}_${article.id}";
            val addArticleToAuthorPipeline = RedisDatabaseManager.jedisPipeline;

            addArticleToAuthorPipeline.hset(articleAuthorRelationRedisSetPrefixName, "article_id", article.id.toString);
            addArticleToAuthorPipeline.hset(
              articleAuthorRelationRedisSetPrefixName,
              "author_id",
              eachAuthor.id.toString
            );

        });
    }
}
