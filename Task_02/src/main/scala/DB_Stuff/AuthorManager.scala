package DB_Stuff

import JsonDefinitions.Author
import JsonDefinitions.AuthorProtocol.authorFormat
import spray.json.enrichAny

object AuthorManager {
    val authorRedisPrefix = "author_"

    /** Add multiple authors to the DB
      *
      * @param authorsToAdd
      *   The list of authors to add.
      */
    def addAuthors(authorsToAdd: List[Author]): Unit = {

        authorsToAdd.foreach(eachAuthor => {
            val authorRedisSetKeyName: String = authorRedisPrefix + eachAuthor.id;
            val authorJsonString: String = eachAuthor.toJson.compactPrint;

            RedisDatabaseManagerWriteMode.jedisPipeline.set(authorRedisSetKeyName, authorJsonString);
        });
    }
}
