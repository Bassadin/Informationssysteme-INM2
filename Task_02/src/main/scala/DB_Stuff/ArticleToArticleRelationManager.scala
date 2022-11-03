package DB_Stuff

import DB_Stuff.RedisDatabaseManager.jedisInstance
import JsonDefinitions.Article

object ArticleToArticleRelationManager {
    final val articleToArticleIndexAutoIncrementKey = "ai_index_relation_article_to_article";

    /** Add to the DB multiple articles that are being referenced by another article.
      *
      * @param referencingArticle
      *   The article that's doing the referencing.
      * @param referencedArticlesIDs
      *   The articles that are being referenced in form of IDs.
      */
    def addArticleToArticlesRelation(referencingArticle: Article, referencedArticlesIDs: List[Long]): Unit = {
        referencedArticlesIDs.foreach(eachReferencedArticleID => {
            val articleToArticleRelationRedisSetPrefixName: String =
                s"relation_article_to_article_${referencingArticle.id}_${eachReferencedArticleID}";

            val addArticleToArticleRelationPipeline = RedisDatabaseManager.jedisPipeline;

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
}
