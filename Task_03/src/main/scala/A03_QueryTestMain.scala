import Additional.LoggingHelper
import Spark.Queries.{QueryManagerArticleCounts, QueryManagerFunctionalAPI, QueryManagerSQL}

object A03_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        // Task 1b)
        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of articles with JSON:");
        println(QueryManagerArticleCounts.articlesCountJSON());
        println(s"Elapsed time: ${LoggingHelper.printElapsedTimeStatusMessage()}");

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of articles with Parquet:");
        println(QueryManagerArticleCounts.articlesCountParquet());
        println(s"Elapsed time: ${LoggingHelper.printElapsedTimeStatusMessage()}");

        // Task 1c)
        println("--- SQL Version ---");

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of distinct authors (SQL):");
        println(QueryManagerSQL.distinctAuthors());
        println(s"Elapsed time: ${LoggingHelper.printElapsedTimeStatusMessage()}");

        LoggingHelper.setStartTimeMilliseconds();
        println("Authors with the most articles (SQL):");
        println("Author with most articles: " + QueryManagerSQL.mostArticles().head);

        println("--- Functional API Version ---");

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of distinct authors (Functional API):");
        println(QueryManagerFunctionalAPI.distinctAuthors());
        println(s"Elapsed time: ${LoggingHelper.printElapsedTimeStatusMessage()}");

        LoggingHelper.setStartTimeMilliseconds();
        println("Authors with the most articles (Functional API):");
        println("Author with most articles: " + QueryManagerFunctionalAPI.mostArticles().head);

        println(
          s"Total elapsed time: ${LoggingHelper.getTimeDifferenceStringBetween(LoggingHelper.millisecondsTimeOnStart)}"
        );
        println("Terminated.");
    }
}
