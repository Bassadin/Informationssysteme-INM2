package DB_Stuff

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
            val authorRedisSetKeyName: String = s"author_${eachAuthor.id}";
            val authorJsonString: String = eachAuthor.toJson.compactPrint;

            RedisDatabaseManagerWriteMode.jedisPipeline.set(authorRedisSetKeyName, authorJsonString);
        });
    }
}
