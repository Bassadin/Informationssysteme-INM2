package DB_Stuff

import JsonDefinitions.Article
import JsonDefinitions.AuthorProtocol.{LongJsonFormat, listFormat}
import spray.json.enrichAny

object ArticleToArticleRelationManager {
    final val articleToArticleRelationRedisPrefix = "relation_article_to_article_";

    /** Add to the DB multiple articles that are being referenced by another article.
      *
      * @param referencingArticle
      *   The article that's doing the referencing.
      * @param referencedArticlesIDs
      *   The articles that are being referenced in form of IDs.
      */
    def addArticleToArticlesRelation(referencingArticle: Article, referencedArticlesIDs: List[Long]): Unit = {
        val articleToArticleRelationRedisSetName: String = articleToArticleRelationRedisPrefix + referencingArticle.id;

        val referencedArticleIDsListJsonString = referencedArticlesIDs.toJson.compactPrint;

        RedisDatabaseManagerWriteMode.jedisPipeline.set(
          articleToArticleRelationRedisSetName,
          referencedArticleIDsListJsonString
        );
    }
}
