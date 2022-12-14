package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.Article
import JsonDefinitions.ArticleProtocol.articleFormat
import spray.json.enrichAny

object ArticleManager extends RedisManagerTrait {
    final val redisPrefix = "arti_";

    /** Add a single article to the DB
      *
      * @param articleToAdd
      *   The Article to add to the DB.
      */
    def addArticle(articleToAdd: Article): Unit = {
        val articleRedisSetKeyName: String = redisPrefix + articleToAdd.id;
        val articleJsonString: String = articleToAdd.toJson.compactPrint;

        RedisDatabaseManagerWriteMode.jedisPipeline.set(articleRedisSetKeyName, articleJsonString);
    }
}
