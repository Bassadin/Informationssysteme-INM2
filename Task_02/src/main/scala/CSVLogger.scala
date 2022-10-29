import com.github.tototoshi.csv.CSVWriter

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object CSVLogger {
    val dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy-hh_mm")
    val CSV_MEASUREMENT_PATH =
        s"./docs/measurements_${dateTimeFormat.format(new Date())}.csv";

    // CSV Stuff
    println("Opening csv file for time logging");
    val csvFile = new File(CSV_MEASUREMENT_PATH);
    val csvWriter: CSVWriter = CSVWriter.open(csvFile);
    csvWriter.writeRow(List("elapsed_time_millis", "stored_entries"));

    /** Writes a row of data into the CSV file
      * @param elapsedMillis
      *   the elapsed milliseconds to write
      * @param indexNumber
      *   the number of read JSON entries that have already been read
      */
    def writeTimeLoggingRow(elapsedMillis: Long, indexNumber: Int): Unit = {
        csvWriter.writeRow(List(elapsedMillis, indexNumber));
    }

    def closeWriter(): Unit = {
        csvWriter.close();
    }
}
