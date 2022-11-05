package DB_Stuff

import JsonDefinitions.{Article, Author}

object AuthorToArticleRelationManager {
    final val authorToArticleRelationRedisPrefix = "relation_author_to_article_";

    /** Add to the DB a relation from an article to multiple authors.
      *
      * @param article
      *   The article to add the authors to.
      * @param authors
      *   The authors to add to the relation.
      */
    def addAuthorToArticleRelation(
        authors: List[Author],
        article: Article
    ): Unit = {
        authors.foreach(eachAuthor => {
            val authorToArticlesRelationRedisSetName: String = authorToArticleRelationRedisPrefix + eachAuthor.id;
            RedisDatabaseManagerWriteMode.jedisPipeline
                .sadd(authorToArticlesRelationRedisSetName, article.id.toString);
        })
    }
}
