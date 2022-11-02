import DB_Stuff.RedisDatabaseManager
import JsonDefinitions.Article
import spray.json._
import JsonDefinitions.ArticleProtocol._

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

        RedisDatabaseManager.addArticle(parsedArticle);

        parsedArticle.authors match {
            case Some(i) =>
                RedisDatabaseManager.addAuthors(i);
                RedisDatabaseManager.addArticleToAuthorsRelation(parsedArticle, i);
            case None =>
        }

        parsedArticle.references match {
            case Some(i) => RedisDatabaseManager.addArticleToArticlesRelation(parsedArticle, i);
            case None    =>
        }
    }
}
