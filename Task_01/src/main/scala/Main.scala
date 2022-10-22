import Helpers.getCurrentTimeStringFrom
import JsonDefinitions.Article
import spray.json._
import scala.io.Source
import JsonDefinitions.ArticleProtocol._
import java.text.SimpleDateFormat

object Main {
    val dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm")

    val JSON_PATH = "./src/data/dblp.v12.json";

    val timeBeforeJson = System.currentTimeMillis();

    def main(args: Array[String]): Unit = {
        // DELETE OLD DB
        DatabaseManager.deleteDBFile();

        println("Starting...");

        val jsonFileSource = Source.fromFile(JSON_PATH);
        val jsonFileLinesIterator = jsonFileSource.getLines;

        // Skip first line, it only contains a [
        jsonFileLinesIterator.next();

        // Use zipWithIndex to get an index iterator alongside the elements
        jsonFileLinesIterator.zipWithIndex.foreach {
            case (eachLineString, indexNumber) =>
                handleLineString(eachLineString);

                // Print a status message every 50k lines
                if (indexNumber % 50000 == 0) {
                    val indexNumberPrintString = String.format(
                      "%,d",
                      indexNumber
                    );
                    println(
                      s"Parsed line $indexNumberPrintString - Elapsed Time: ${getCurrentTimeStringFrom(timeBeforeJson)}"
                    );

                    val elapsedMillis =
                        System.currentTimeMillis() - timeBeforeJson;
                    CSVLogger.writeTimeLoggingRow(elapsedMillis, indexNumber);
                }
        };
        println("Finished parsing JSON file.");

        // Enable article refs FK check after all data is inserted
        val timeBeforeFKEnabling = System.currentTimeMillis();
        println("Enabling FK checks...");
        DatabaseManager.enableArticleRefsForeignKeyCheck();
        println(
          s"Enabling FK checks finished in ${getCurrentTimeStringFrom(timeBeforeFKEnabling)}."
        );

        DatabaseManager.closeConnection;
        jsonFileSource.close();

        println(
          s"Total elapsed time: ${getCurrentTimeStringFrom(timeBeforeJson)}"
        );
        println("Terminated.");
    }

    def handleLineString(eachLineString: String): Unit = {
        // Terminate for last line
        if (eachLineString.charAt(0) == ']') {
            return;
        }

        val cleanedLineString = eachLineString
            // Replace non-printable character with ?
            .replace("\uFFFF", "?")
            .replaceFirst("^,", "");
        val parsedArticle: Article =
            cleanedLineString.parseJson.convertTo[Article];

        DatabaseManager.addArticle(parsedArticle);

        if (parsedArticle.authors.isDefined) {
            DatabaseManager.addAuthors(parsedArticle.authors.get);
            DatabaseManager.addArticleToAuthorsRelation(
              parsedArticle,
              parsedArticle.authors.get
            );
        }

        if (parsedArticle.references.isDefined) {
            DatabaseManager.addArticleToArticlesRelation(
              parsedArticle,
              parsedArticle.references.get
            );
        }
    }
}
