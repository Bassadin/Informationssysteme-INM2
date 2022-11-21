package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.Author
import JsonDefinitions.AuthorProtocol.authorFormat
import spray.json.enrichAny

object AuthorManager extends RedisManagerTrait {
    val redisPrefix = "author_"

    val AUTHORS_IDS_PF_SET_KEY: String = redisPrefix + "ids_pf_set";

    /** Add multiple authors to the DB
      *
      * @param authorsToAdd
      *   The list of authors to add.
      */
    def addAuthors(authorsToAdd: List[Author]): Unit = {
        authorsToAdd.foreach(eachAuthor => {
            val authorJsonString: String = eachAuthor.toJson.compactPrint;

            RedisDatabaseManagerWriteMode.jedisPipeline.set(redisPrefix + eachAuthor.id, authorJsonString);

            val authorIdString = eachAuthor.id.toString;

            // HyperLogLog
            RedisDatabaseManagerWriteMode.jedisPipeline.pfadd(AUTHORS_IDS_PF_SET_KEY, authorIdString);
        });
    }
}
