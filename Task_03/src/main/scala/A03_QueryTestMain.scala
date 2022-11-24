import Additional.LoggingHelper
import Additional.LoggingHelper.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DataClasses.Author
import Spark.Queries.QueryManagerFunctionalAPI

object A03_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        LoggingHelper.setInitialStartTimeMilliseconds();

        println("Amount of distinct authors:");
        println(QueryManagerFunctionalAPI.distinctAuthors());
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Authors with the most articles:");
        val authorWithMostArticles: Author = QueryManagerFunctionalAPI.mostArticles().head;
        println("Author with most articles: " + authorWithMostArticles);

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
