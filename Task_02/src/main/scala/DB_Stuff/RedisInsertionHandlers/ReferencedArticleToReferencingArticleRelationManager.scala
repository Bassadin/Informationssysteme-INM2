package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.Article

object ReferencedArticleToReferencingArticleRelationManager extends RedisManagerTrait {
    final val redisPrefix = "relation_referenced_article_to_referencing_article_";

    /** Add to the DB multiple articles that are being referenced by another article.
      *
      * @param referencingArticle
      *   The article that's doing the referencing.
      * @param referencedArticlesIDs
      *   The articles that are being referenced in form of IDs.
      */
    def addRelation(referencedArticlesIDs: List[Long], referencingArticle: Article): Unit = {
        referencedArticlesIDs.foreach(eachReferencedArticlesID => {
            val redisSaddKey: String = redisPrefix + eachReferencedArticlesID;
            RedisDatabaseManagerWriteMode.jedisPipeline.sadd(redisSaddKey, referencingArticle.id.toString);
        })
    }
}
