import Helpers.getCurrentTimeStringFrom
import com.github.tototoshi.csv._
import spray.json._

import java.io.File
import scala.io.Source
import MyJsonProtocol._

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

object Main {
    val dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm")

    val JSON_PATH = "./src/data/dblp.v12.json";
    val DB_PATH = "./demo.mv.db"
    val CSV_MEASUREMENT_PATH = s"./docs/measurements_${dateTimeFormat.format(new Date())}.csv";

    def main(args: Array[String]): Unit = {
        println("Deleting old db...");
        new File(DB_PATH).delete()

        println("Starting...");

        //        if (debugMode) {
        //            println(s"Amount of line in file: ${io.Source.fromFile(JSON_PATH).getLines.size}");
        //        }

        // CSV Stuff
        val csvFile = new File(CSV_MEASUREMENT_PATH);
        val csvWriter = CSVWriter.open(csvFile);
        csvWriter.writeRow(List("elapsed_time_millis", "stored_entries"));

        val timeBeforeJson = System.currentTimeMillis();

        val jsonFileLinesIterator = Source.fromFile(JSON_PATH).getLines;

        // Skip first line, it only contains a [
        jsonFileLinesIterator.next();

        jsonFileLinesIterator.zipWithIndex.foreach { case (eachLineString, indexNumber) =>
            // Terminate for last line
            if (eachLineString.charAt(0) == ']') {
                return;
            }

            val cleanedLineString = eachLineString
                .replace("\uFFFF", "")
                .replaceFirst("^,", "");
            val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

            DatabaseManager.addArticle(parsedArticle);

            if (parsedArticle.authors.isDefined) {
                DatabaseManager.addAuthors(parsedArticle.authors.get);
                DatabaseManager.addArticleToAuthorsRelation(parsedArticle, parsedArticle.authors.get);
            }

            if (parsedArticle.references.isDefined) {
                // TODO wie Problem mit FK constraint lösen?
                DatabaseManager.addArticleToArticlesRelation(parsedArticle, parsedArticle.references.get);
            }

            // Print a status message every 50k lines
            if (indexNumber % 50000 == 0) {
                println("Parsed line " + String.format("%,d", indexNumber) + " - Elapsed Time: " + getCurrentTimeStringFrom(timeBeforeJson));
                csvWriter.writeRow(List(System.currentTimeMillis() - timeBeforeJson, indexNumber));
            }
        };

        csvWriter.close();
        DatabaseManager.closeConnection;

        println("Total elapsed time: " + getCurrentTimeStringFrom(timeBeforeJson));
        println("Terminated.");
    }
}