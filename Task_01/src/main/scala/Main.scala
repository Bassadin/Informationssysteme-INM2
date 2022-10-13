import scala.io.Source
import spray.json._

import java.text.Normalizer
import scala.util.control.Breaks.break

case class Author(id: Long, name: String, org: Option[String]);

case class Article(
                      id: Long,
                      authors: List[Author],
                      title: String,
                      year: Int,
                      n_citation: Int,
                      page_start: String,
                      page_end: String,
                      doc_type: Option[String],
                      publisher: String,
                      volume: String,
                      issue: String,
                      DOI: Option[String]);

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
        "DOI")
}

object Main {

    import MyJsonProtocol._

    final val JSON_PATH: String = "./src/data/dblp.v12.json";


    def main(args: Array[String]): Unit = {
        println("Starting...")
        DatabaseManager.createDatabases;

        val timeBeforeJson = System.nanoTime();

        // JSON stuff
        val fileLines = Source.fromFile(JSON_PATH).getLines();

        // Skip first line, it only contains a [
        fileLines.next();

        fileLines.zipWithIndex.foreach { case (eachLineString, indexNumber) =>
            if (eachLineString.charAt(0) == ']') {
                return;
            }

            // https://stackoverflow.com/a/5462747/3526350
            val normalizedLineString: String = Normalizer.normalize(eachLineString, Normalizer.Form.NFKC);

            val cleanedLineString: String = normalizedLineString.replaceFirst("^,", "");
            val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

            parsedArticle.authors.foreach(eachAuthor => {
                DatabaseManager.addAuthor(eachAuthor);
            })

            DatabaseManager.addArticle(parsedArticle);

            // TODO also add relations

            // Print a status message every 10k lines
            if (indexNumber % 10000 == 0) {
                println("Parsed line " + indexNumber);
            }
        };

        val timeAfterJson = System.nanoTime();

        println("Elapsed time: " + (timeAfterJson - timeBeforeJson) / 1000000000 + "s");

        DatabaseManager.closeConnection;
        println("Terminated.");
    }
}