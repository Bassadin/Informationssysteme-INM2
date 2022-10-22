import scala.io.Source

object Helpers {
    def getCurrentTimeStringFrom(startTime: Long): String = {
        val currentTime = System.currentTimeMillis();

        val timeDifference = (currentTime - startTime);

        val minutes = (timeDifference / 1000) / 60
        val seconds = (timeDifference / 1000) % 60

        s"${minutes}m ${seconds}s";
    }

    def printElapsedTimeStatusMessage(indexNumber: Int, referenceTimestamp: Long): Unit = {
        val indexNumberPrintString = String.format("%,d", indexNumber);
        println(s"Parsed line $indexNumberPrintString - Elapsed Time: ${getCurrentTimeStringFrom(referenceTimestamp)}");
    }
}
