import scala.io.Source
import spray.json._

import java.text.Normalizer
import scala.util.control.Breaks.break

case class Author(id: Long, name: String, org: Option[String]);

case class Article(
                      id: Long,
                      authors: Option[List[Author]],
                      title: String,
                      year: Int,
                      n_citation: Int,
                      page_start: String,
                      page_end: String,
                      doc_type: Option[String],
                      publisher: String,
                      volume: String,
                      issue: String,
                      DOI: Option[String],
                      references: Option[List[Long]]);

object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val authorFormat = jsonFormat(Author, "id", "name", "org")
    implicit val articleFormat = jsonFormat(
        Article, "id",
        "authors",
        "title",
        "year",
        "n_citation",
        "page_start",
        "page_end",
        "doc_type",
        "publisher",
        "volume",
        "issue",
        "DOI",
        "references")
}

object Main {

    import MyJsonProtocol._

    final val JSON_PATH: String = "./src/data/dblp.v12.json";


    def main(args: Array[String]): Unit = {
        println("Starting...")

        val timeBeforeJson = System.nanoTime();

        // JSON stuff
        val fileLines = Source.fromFile(JSON_PATH).getLines();

        // Skip first line, it only contains a [
        fileLines.next();

        fileLines.zipWithIndex.foreach { case (eachLineString, indexNumber) =>
            if (eachLineString.charAt(0) == ']') {
                return;
            }

            val normalizedLineString: String = eachLineString.replace("\uFFFF", "");
            val cleanedLineString: String = normalizedLineString.replaceFirst("^,", "");
            val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

            DatabaseManager.addArticle(parsedArticle);

            if (parsedArticle.authors.isDefined) {
                DatabaseManager.addAuthors(parsedArticle.authors.get);
                DatabaseManager.addArticleToAuthorsRelation(parsedArticle, parsedArticle.authors.get);
            }

            if (parsedArticle.references.isDefined) {
                // DatabaseManager.addArticleToArticlesRelation(parsedArticle, parsedArticle.references);
            }

            // Print a status message every 10k lines
            if (indexNumber % 10000 == 0) {
                println("Parsed line " + String.format("%,d", indexNumber) + " - Elapsed Time: " + printCurrentTimeFrom(timeBeforeJson));
            }
        };

        DatabaseManager.closeConnection;

        println("Total elapsed time: " + printCurrentTimeFrom(timeBeforeJson));

        println("Terminated.");
    }

    def printCurrentTimeFrom(startTime: Long): String = {
        val currentTime = System.nanoTime();
        // https://stackoverflow.com/a/12682507/3526350
        return String.format("%,d", (currentTime - startTime) / 1000000000) + "s";
    }
}