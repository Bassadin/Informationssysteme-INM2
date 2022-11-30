import Additional.LoggingHelper
import Spark.ParquetReader
import Spark.Queries.{QueryManagerArticleCounts, QueryManagerFunctionalAPI, QueryManagerSQL}

object A03_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        ParquetReader.createOrReplaceArticlesView();

        // Task 1b)
        println("\n\n--- Article counts ---");

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of articles with JSON:");
        println(QueryManagerArticleCounts.articlesCountJSON());
        LoggingHelper.printElapsedTimeStatusMessage();
        println();

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of articles with Parquet:");
        println(QueryManagerArticleCounts.articlesCountParquet());
        LoggingHelper.printElapsedTimeStatusMessage();
        println();

        // Task 1c)
        println("\n\n--- Functional API Version ---");

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of distinct authors (Functional API):");
        println(QueryManagerFunctionalAPI.distinctAuthors());
        LoggingHelper.printElapsedTimeStatusMessage();
        println();

        LoggingHelper.setStartTimeMilliseconds();
        println("Authors with the most articles (Functional API):");
        println("Author with most articles: " + QueryManagerFunctionalAPI.mostArticles().head);
        println();

        println("\n\n--- SQL Version ---");

        LoggingHelper.setStartTimeMilliseconds();
        println("Amount of distinct authors (SQL):");
        println(QueryManagerSQL.distinctAuthors());
        LoggingHelper.printElapsedTimeStatusMessage();

        LoggingHelper.setStartTimeMilliseconds();
        println("Authors with the most articles (SQL):");
        println("Author with most articles: " + QueryManagerSQL.mostArticles().head);

        println(
          s"Total elapsed time: ${LoggingHelper.getTimeDifferenceStringBetween(LoggingHelper.millisecondsTimeOnStart)}"
        );
        println("Terminated.");
    }
}
