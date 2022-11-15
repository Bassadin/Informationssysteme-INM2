import Additional.LoggingHelper.{LOGGING_FREQUENCY_LINES, getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import Additional.{LoggingHelper, Parsing}
import DB_Stuff.RedisDatabaseManagerWriteMode

import scala.io.Source

object A03_ParseJsonMain {
    val JSON_PATH: String = "./src/data/dblp.v12.json";

    def main(args: Array[String]): Unit = {
        // Measure time before starting as reference timeframe

        LoggingHelper.setInitialStartTimeMilliseconds();

        println("Starting Task_03...");

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
                    LoggingHelper.printElapsedTimeStatusMessage(eachIndex);
                }
            }
        };
        println("Finished parsing JSON file.");

        jsonFileSource.close();

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
