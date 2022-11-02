import DB_Stuff.RedisDatabaseManager
import Helpers.getCurrentTimeStringFrom

import scala.io.Source

object Main_A02 {
    val JSON_PATH = "./src/data/dblp.v12.json";
    val LOGGING_FREQUENCY_LINES = 2_000;

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
            Parsing.handleLineString(eachLineString);

            if (eachIndex % LOGGING_FREQUENCY_LINES == 0) {
                Helpers.printElapsedTimeStatusMessage(eachIndex, millisecondsTimeOnStart);

                val elapsedMilliseconds = System.currentTimeMillis() - millisecondsTimeOnStart;
                CSVLogger.writeTimeLoggingRow(elapsedMilliseconds, eachIndex);
            }
        };
        println("Finished parsing JSON file.");

        jsonFileSource.close();
        RedisDatabaseManager.closeConnection();

        println(s"Total elapsed time: ${getCurrentTimeStringFrom(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
