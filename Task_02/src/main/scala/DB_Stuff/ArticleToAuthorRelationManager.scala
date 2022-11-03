package DB_Stuff

import JsonDefinitions.AuthorProtocol.{LongJsonFormat, listFormat}
import JsonDefinitions.{Article, Author}
import spray.json.enrichAny

object ArticleToAuthorRelationManager {
    final val authorArticleIndexAutoIncrementKey = "ai_index_relation_author_to_article";

    /** Add to the DB a relation from an article to multiple authors.
      *
      * @param article
      *   The article to add the authors to.
      * @param authors
      *   The authors to add to the relation.
      */
    def addArticleToAuthorsRelation(
        article: Article,
        authors: List[Author]
    ): Unit = {
        val articleToAuthorRelationRedisSetName: String = s"relation_article_to_article_${article.id}";

        val authorIDsListJsonString = authors.map(author => author.id).toJson.compactPrint;

        RedisDatabaseManagerWriteMode.jedisPipeline.set(
          articleToAuthorRelationRedisSetName,
          authorIDsListJsonString
        );
    }
}
