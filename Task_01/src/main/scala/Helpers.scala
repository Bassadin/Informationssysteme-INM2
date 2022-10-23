import scala.io.Source

object Helpers {

    /** Get the passed time from a given time in the format `[minutes]m [seconds]s`
      * @param startTimeMillis
      *   The reference time to get the difference from in milliseconds
      * @return
      *   The formatted time difference
      */
    def getCurrentTimeStringFrom(startTimeMillis: Long): String = {
        val currentTimeMillis = System.currentTimeMillis();

        val timeDifferenceMillis = currentTimeMillis - startTimeMillis;

        val minutes = (timeDifferenceMillis / 1000) / 60
        val seconds = (timeDifferenceMillis / 1000) % 60

        s"${minutes}m ${seconds}s";
    }

    /** Prints a status message with the elapsed time.
      * @param indexNumber
      *   How many JSON entries have been read.
      * @param referenceTimestampMillis
      *   The reference time to get the difference from
      */
    def printElapsedTimeStatusMessage(indexNumber: Int, referenceTimestampMillis: Long): Unit = {
        val indexNumberPrintString = String.format("%,d", indexNumber);
        println(
          s"| - Parsed line $indexNumberPrintString - Elapsed Time: ${getCurrentTimeStringFrom(referenceTimestampMillis)}"
        );
    }
}
