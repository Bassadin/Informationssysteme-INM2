import Main_A02.millisecondsTimeOnStart

import scala.io.Source

object Helpers {

    /** Get the passed time from a given time in the format `[minutes]m [seconds]s`
      * @param endTimeMillis
      *   The reference time to get the difference from in milliseconds
      * @return
      *   The formatted time difference
      */
    def getTimeDifferenceStringBetween(
        endTimeMillis: Long,
        referenceStartTime: Long = System.currentTimeMillis()
    ): String = {
        val timeDifferenceMillis = referenceStartTime - endTimeMillis;

        val minutes = (timeDifferenceMillis / 1000) / 60
        val seconds = (timeDifferenceMillis / 1000) % 60

        s"${minutes}m ${seconds}s";
    }

    var lastLineTimestamp: Long = 0L;

    /** Prints a status message with the elapsed time.
      * @param indexNumber
      *   How many JSON entries have been read.
      */
    def printElapsedTimeStatusMessage(indexNumber: Int): Unit = {
        val indexNumberPrintString = String.format("%,d", indexNumber);
        val elapsedTimeString = getTimeDifferenceStringBetween(millisecondsTimeOnStart);
        val deltaTimeString =
            if (lastLineTimestamp == 0) "0"
            else getTimeDifferenceStringBetween(lastLineTimestamp, System.currentTimeMillis());

        println(
          s"| - Parsed line $indexNumberPrintString - Elapsed Time: $elapsedTimeString (+$deltaTimeString)"
        );

        lastLineTimestamp = System.currentTimeMillis();
    }
}
