package DB_Stuff

import DB_Stuff.RedisDatabaseManager.jedisInstance
import JsonDefinitions.Author

object AuthorManager {
    /** Add multiple authors to the DB
     *
     * @param authorsToAdd
     * The list of authors to add.
     */
    def addAuthors(authorsToAdd: List[Author]): Unit = {

        authorsToAdd.foreach(eachAuthor => {
            val authorRedisSetPrefixName: String = s"author_${eachAuthor.id}";

            // Return out if author key already exists
            if (jedisInstance.keys(authorRedisSetPrefixName).size() > 0) {
                return;
            }

            val addAuthorsPipeline = jedisInstance.pipelined();

            addAuthorsPipeline.hset(authorRedisSetPrefixName, "title", eachAuthor.name);

            eachAuthor.org match {
                case Some(i) => addAuthorsPipeline.hset(authorRedisSetPrefixName, "org", i);
                case None =>
            }

            addAuthorsPipeline.sync();
        });
    }
}
