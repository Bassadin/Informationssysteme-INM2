package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.Article
import JsonDefinitions.AuthorProtocol.{LongJsonFormat, listFormat}
import spray.json.enrichAny

object ReferencingArticleToReferencedArticleRelationManager {
    final val redisPrefix = "r_referencing_arti-referenced_arti_";

    /** Add to the DB multiple referenced articles that are being referenced by another article.
      *
      * @param referencingArticle
      *   The article that's doing the referencing.
      * @param referencedArticlesIDs
      *   The articles that are being referenced in form of IDs.
      */
    def addRelation(referencingArticle: Article, referencedArticlesIDs: List[Long]): Unit = {
        val articleToArticleRelationRedisSetName: String = redisPrefix + referencingArticle.id;

        val referencedArticleIDsListJsonString = referencedArticlesIDs.toJson.compactPrint;

        RedisDatabaseManagerWriteMode.jedisPipeline.set(
          articleToArticleRelationRedisSetName,
          referencedArticleIDsListJsonString
        );
    }
}
