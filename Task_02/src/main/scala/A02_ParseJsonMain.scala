import Additional.Helpers.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import Additional.{CSVLogger, Helpers, Parsing}
import DB_Stuff.RedisDatabaseManagerWriteMode

import scala.io.Source

object A02_ParseJsonMain {
    val JSON_PATH = "./src/data/dblp.v12.json";
    val LOGGING_FREQUENCY_LINES = 20_000;

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

            if (eachIndex > 0) {
                if (eachIndex % LOGGING_FREQUENCY_LINES == 0) {
                    Helpers.printElapsedTimeStatusMessage(eachIndex);

                    val elapsedMilliseconds = System.currentTimeMillis() - millisecondsTimeOnStart;
                    CSVLogger.writeTimeLoggingRow(elapsedMilliseconds, eachIndex);
                }
                // Sync the pipeline every x lines to save on client RAM
                if (eachIndex % RedisDatabaseManagerWriteMode.PIPELINE_SYNC_LINE_FREQUENCY == 0) {
                    RedisDatabaseManagerWriteMode.jedisPipeline.sync();
                }
            }
        };
        println("Finished parsing JSON file.");

        jsonFileSource.close();
        RedisDatabaseManagerWriteMode.syncPipelineAndCloseConnection();

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
