package DB_Stuff

import JsonDefinitions.Article
import JsonDefinitions.AuthorProtocol.{LongJsonFormat, listFormat}
import spray.json.enrichAny

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
        val articleToArticleRelationRedisSetName: String =
            s"relation_article_to_article_${referencingArticle.id}";

        val referencedArticleIDsListJsonString = referencedArticlesIDs.toJson.compactPrint;

        RedisDatabaseManager.jedisPipeline.set(
          articleToArticleRelationRedisSetName,
          referencedArticleIDsListJsonString
        );
    }
}
