import Additional.{CSVLogger, Helpers, Parsing}
import DB_Stuff.RedisDatabaseManagerWriteMode
import Additional.Helpers.getTimeDifferenceStringBetween

import scala.io.Source

object A02_Main {
    val JSON_PATH = "./src/data/dblp.v12.json";
    val LOGGING_FREQUENCY_LINES = 50_000;

    val millisecondsTimeOnStart: Long = System.currentTimeMillis();

    def main(args: Array[String]): Unit = {
        // Measure time before starting as reference timeframe

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
                Helpers.printElapsedTimeStatusMessage(eachIndex);

                val elapsedMilliseconds = System.currentTimeMillis() - millisecondsTimeOnStart;
                CSVLogger.writeTimeLoggingRow(elapsedMilliseconds, eachIndex);
            }
        };
        println("Finished parsing JSON file.");

        jsonFileSource.close();
        RedisDatabaseManagerWriteMode.syncPipelineAndCloseConnection();

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
