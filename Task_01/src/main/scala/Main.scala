import Helpers.{getCurrentTimeStringFrom}

import scala.io.Source

import MyJsonProtocol._

object Main {

    val JSON_PATH: String = "./src/data/dblp.v12.json";

    val debugMode = true;

    def main(args: Array[String]): Unit = {
        println("Starting...")

//        if (debugMode) {
//            println(s"Amount of line in file: ${io.Source.fromFile(JSON_PATH).getLines.size}");
//        }

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

            // Print a status message every 10k lines
            if (debugMode && indexNumber % 10000 == 0) {
                println("Parsed line " + String.format("%,d", indexNumber) + " - Elapsed Time: " + getCurrentTimeStringFrom(timeBeforeJson));
            }
        };

        DatabaseManager.closeConnection;

        println("Total elapsed time: " + getCurrentTimeStringFrom(timeBeforeJson));
        println("Terminated.");
    }
}