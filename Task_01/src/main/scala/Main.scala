import Helpers.getCurrentTimeStringFrom
import JsonDefinitions.Article
import spray.json._
import scala.io.Source
import JsonDefinitions.ArticleProtocol._
import java.text.SimpleDateFormat

object Main {
    val JSON_PATH = "./src/data/dblp.v12.json";

    def main(args: Array[String]): Unit = {
        // Measure time before starting as reference timeframe
        val millisecondsTimeOnStart = System.currentTimeMillis();

        println("Starting...");

        val jsonFileSource = Source.fromFile(JSON_PATH);
        val jsonFileLinesIterator = jsonFileSource.getLines;

        println(s"--- Starting to parse json file '$JSON_PATH' ---");

        // Skip first line, it only contains a [
        jsonFileLinesIterator.next();

        // Use zipWithIndex to get an index iterator alongside the elements
        jsonFileLinesIterator.zipWithIndex.foreach { case (eachLineString, eachIndex) =>
            handleLineString(eachLineString);

            // Print a status message every 50k lines
            if (eachIndex % 50000 == 0) {
                Helpers.printElapsedTimeStatusMessage(eachIndex, millisecondsTimeOnStart);

                val elapsedMilliseconds = System.currentTimeMillis() - millisecondsTimeOnStart;
                CSVLogger.writeTimeLoggingRow(elapsedMilliseconds, eachIndex);
            }
        };
        println("Finished parsing JSON file.");

        // Enable article refs FK check after all data is inserted
        DatabaseManager.enableArticleRefsForeignKeyCheck();

        DatabaseManager.closeConnection();
        jsonFileSource.close();

        println(s"Total elapsed time: ${getCurrentTimeStringFrom(millisecondsTimeOnStart)}");
        println("Terminated.");
    }

    /** Handle a json line string for insertion in the db
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
