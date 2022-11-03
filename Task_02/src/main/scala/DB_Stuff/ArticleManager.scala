package DB_Stuff

import DB_Stuff.RedisDatabaseManager.jedisInstance
import JsonDefinitions.Article

object ArticleManager {
    /** Add a single article to the DB
     *
     * @param articleToAdd
     * The Article to add to the DB.
     */
    def addArticle(articleToAdd: Article): Unit = {

        val addArticlePipeline = RedisDatabaseManager.jedisPipeline;

        val articleRedisSetPrefixName: String = s"article_${articleToAdd.id}";
        addArticlePipeline.hset(articleRedisSetPrefixName, "title", articleToAdd.title);
        addArticlePipeline.hset(articleRedisSetPrefixName, "year", articleToAdd.year.toString);
        addArticlePipeline.hset(articleRedisSetPrefixName, "n_citation", articleToAdd.n_citation.toString);
        addArticlePipeline.hset(articleRedisSetPrefixName, "page_start", articleToAdd.page_start);
        addArticlePipeline.hset(articleRedisSetPrefixName, "page_start", articleToAdd.page_start);

        articleToAdd.doc_type match {
            case Some(i) => addArticlePipeline.hset(articleRedisSetPrefixName, "doc_type", i);
            case None =>
        }

        addArticlePipeline.hset(articleRedisSetPrefixName, "publisher", articleToAdd.publisher);
        addArticlePipeline.hset(articleRedisSetPrefixName, "volume", articleToAdd.volume);
        addArticlePipeline.hset(articleRedisSetPrefixName, "issue", articleToAdd.issue);

        articleToAdd.DOI match {
            case Some(i) => addArticlePipeline.hset(articleRedisSetPrefixName, "DOI", i);
            case None =>
        }

        addArticlePipeline.sync();
    }
}
