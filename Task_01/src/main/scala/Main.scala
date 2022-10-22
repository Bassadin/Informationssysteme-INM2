import Helpers.getCurrentTimeStringFrom
import JsonDefinitions.Article
import spray.json._
import scala.io.Source
import JsonDefinitions.ArticleProtocol._
import java.text.SimpleDateFormat

object Main {
    val dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm")

    val JSON_PATH = "./src/data/dblp.v12.json";

    def main(args: Array[String]): Unit = {
        // Measure time before starting as reference timeframe
        val timeBeforeJson = System.currentTimeMillis();

        // DELETE OLD DB
        DatabaseManager.deleteDBFile();

        println("Starting...");

        val jsonFileSource = Source.fromFile(JSON_PATH);
        val jsonFileLinesIterator = jsonFileSource.getLines;

        // Skip first line, it only contains a [
        jsonFileLinesIterator.next();

        // Use zipWithIndex to get an index iterator alongside the elements
        jsonFileLinesIterator.zipWithIndex.foreach { case (eachLineString, indexNumber) =>
            handleLineString(eachLineString);

            // Print a status message every 50k lines
            if (indexNumber % 50000 == 0) {
                Helpers.printElapsedTimeStatusMessage(indexNumber, timeBeforeJson);

                val elapsedMillis = System.currentTimeMillis() - timeBeforeJson;
                CSVLogger.writeTimeLoggingRow(elapsedMillis, indexNumber);
            }
        };
        println("Finished parsing JSON file.");

        // Enable article refs FK check after all data is inserted
        DatabaseManager.enableArticleRefsForeignKeyCheck();

        DatabaseManager.closeConnection;
        jsonFileSource.close();

        println(s"Total elapsed time: ${getCurrentTimeStringFrom(timeBeforeJson)}");
        println("Terminated.");
    }

    def handleLineString(eachLineString: String): Unit = {
        // Terminate for last line
        if (eachLineString.charAt(0) == ']') {
            return;
        }

        val cleanedLineString = eachLineString.replace("\uFFFF", "?").replaceFirst("^,", "");
        val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

        DatabaseManager.addArticle(parsedArticle);

        if (parsedArticle.authors.isDefined) {
            DatabaseManager.addAuthors(parsedArticle.authors.get);
            DatabaseManager.addArticleToAuthorsRelation(parsedArticle, parsedArticle.authors.get);
        }

        if (parsedArticle.references.isDefined) {
            DatabaseManager.addArticleToArticlesRelation(parsedArticle, parsedArticle.references.get);
        }
    }
}
