package DB_Stuff

import JsonDefinitions.Article
import JsonDefinitions.ArticleProtocol.articleFormat
import spray.json.enrichAny

object ArticleManager {
    final val articleRedisPrefix = "article_";

    /** Add a single article to the DB
      *
      * @param articleToAdd
      *   The Article to add to the DB.
      */
    def addArticle(articleToAdd: Article): Unit = {
        val articleRedisSetKeyName: String = articleRedisPrefix + articleToAdd.id;
        val articleJsonString: String = articleToAdd.toJson.compactPrint;

        RedisDatabaseManagerWriteMode.jedisPipeline.set(articleRedisSetKeyName, articleJsonString);
    }
}
