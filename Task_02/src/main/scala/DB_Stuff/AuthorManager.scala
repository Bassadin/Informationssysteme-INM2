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
            val addAuthorsPipeline = RedisDatabaseManager.jedisPipeline;

            addAuthorsPipeline.hset(authorRedisSetPrefixName, "title", eachAuthor.name);

            eachAuthor.org match {
                case Some(i) => addAuthorsPipeline.hset(authorRedisSetPrefixName, "org", i);
                case None =>
            }

        });
    }
}
