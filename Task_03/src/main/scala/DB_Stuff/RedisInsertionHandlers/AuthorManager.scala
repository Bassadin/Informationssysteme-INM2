package DB_Stuff.RedisInsertionHandlers

import DB_Stuff.RedisDatabaseManagerWriteMode
import JsonDefinitions.Author
import JsonDefinitions.AuthorProtocol.authorFormat
import spray.json.enrichAny

object AuthorManager {
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

            // Exact size set
            RedisDatabaseManagerWriteMode.jedisPipeline.sadd(AUTHORS_IDS_EXACT_SET_KEY, authorIdString);

            // HyperLogLog
            RedisDatabaseManagerWriteMode.jedisPipeline.pfadd(AUTHORS_IDS_PF_SET_KEY, authorIdString);
        });
    }
}
