import Helpers.getCurrentTimeStringFrom
import JsonDefinitions.Article
import spray.json._
import scala.io.Source
import JsonDefinitions.ArticleProtocol._
import java.text.SimpleDateFormat

object Main {
    val JSON_PATH = "./src/data/dblp.v12.json";
    val LOGGING_FREQUENCY_LINES = 200_000;

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
            if (eachIndex % LOGGING_FREQUENCY_LINES == 0) {
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

        // TODO Remove fos and others with regex (no performance improvements... :( )
        // .replace("""(?:"fos":\[.*?\],|"indexed_abstract":\{.*\},|"venue":\{.*?\})""", "");

        val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

        DatabaseManager.addArticle(parsedArticle);

        parsedArticle.authors match {
            case Some(i) =>
                DatabaseManager.addAuthors(i);
                DatabaseManager.addArticleToAuthorsRelation(parsedArticle, i);
            case None =>
        }

        parsedArticle.references match {
            case Some(i) => DatabaseManager.addArticleToArticlesRelation(parsedArticle, i);
            case None    =>
        }
    }
}
