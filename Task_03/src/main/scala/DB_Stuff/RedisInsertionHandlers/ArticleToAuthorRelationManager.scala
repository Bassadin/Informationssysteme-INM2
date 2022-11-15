package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.AuthorProtocol.{LongJsonFormat, listFormat}
import JsonDefinitions.{Article, Author}
import spray.json.enrichAny

object ArticleToAuthorRelationManager extends RedisManagerTrait {
    final val redisPrefix = "r_arti-author_";

    /** Add to the DB a relation from an article to multiple authors.
      *
      * @param article
      *   The article to add the authors to.
      * @param authors
      *   The authors to add to the relation.
      */
    def addRelation(
        article: Article,
        authors: List[Author]
    ): Unit = {
        val articleToAuthorRelationRedisSetName: String = redisPrefix + article.id;

        val authorIDsListJsonString = authors.map(author => author.id).toJson.compactPrint;

        RedisDatabaseManagerWriteMode.jedisPipeline.set(
          articleToAuthorRelationRedisSetName,
          authorIDsListJsonString
        );
    }
}
