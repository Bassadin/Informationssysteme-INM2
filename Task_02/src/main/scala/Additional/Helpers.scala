package Additional

import java.time.Duration

object Helpers {

    private final val REFERENCE_FILE_LINES_AMOUNT = 4_900_000;

    private var lastLineTimestamp: Long = 0L;

    var millisecondsTimeOnStart: Long = System.currentTimeMillis();

    setInitialStartTimeMilliseconds();

    def setInitialStartTimeMilliseconds(): Unit = {
        millisecondsTimeOnStart = System.currentTimeMillis();
    }

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

        val completionPercentage = indexNumber / REFERENCE_FILE_LINES_AMOUNT;

        println(
          s"| - Parsed line $indexNumberPrintString ($completionPercentage%) - Elapsed Time: $elapsedTimeString (+$deltaTimeString)"
        );

        lastLineTimestamp = System.currentTimeMillis();
    }

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
        val duration = Duration.ofMillis(referenceStartTime - endTimeMillis);

        s"${duration.toMinutesPart}m ${duration.toSecondsPart}s ${duration.toMillisPart}ms";
    }
}
