import Main.{dateTimeFormat}
import com.github.tototoshi.csv.CSVWriter

import java.io.File
import java.util.Date

object CSVLogger {
    val CSV_MEASUREMENT_PATH =
        s"./docs/measurements_${dateTimeFormat.format(new Date())}.csv";

    // CSV Stuff
    println("Opening csv file for time logging");
    val csvFile = new File(CSV_MEASUREMENT_PATH);
    val csvWriter = CSVWriter.open(csvFile);
    csvWriter.writeRow(List("elapsed_time_millis", "stored_entries"));

    def writeTimeLoggingRow(elapsedMillis: Long, indexNumber: Int): Unit = {
        csvWriter.writeRow(List(elapsedMillis, indexNumber));
    }

    def closeWriter() = {
        csvWriter.close();
    }
}
