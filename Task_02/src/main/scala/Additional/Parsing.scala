package Additional

import DB_Stuff.{ArticleManager, ArticleToArticleRelationManager, ArticleToAuthorRelationManager, AuthorManager}
import spray.json._
import JsonDefinitions.Article
import JsonDefinitions.ArticleProtocol.articleFormat

object Parsing {

    /** Handle a json line string for insertion into redis
      *
      * @param eachLineString
      *   The string to compute
      */
    def handleLineString(eachLineString: String): Unit = {
        // Return for last line
        if (eachLineString.charAt(0) == ']') {
            return;
        }

        val cleanedLineString = eachLineString.replace("\uFFFF", "?").replaceFirst("^,", "");
        val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

        ArticleManager.addArticle(parsedArticle);

        parsedArticle.authors match {
            case Some(i) =>
                AuthorManager.addAuthors(i);
                ArticleToAuthorRelationManager.addArticleToAuthorsRelation(parsedArticle, i);
            case None =>
        }

        parsedArticle.references match {
            case Some(i) => ArticleToArticleRelationManager.addArticleToArticlesRelation(parsedArticle, i);
            case None    =>
        }
    }
}
