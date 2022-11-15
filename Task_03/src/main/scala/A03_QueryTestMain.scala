import Additional.Helpers
import Additional.Helpers.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DB_Stuff.{QueryManager, RedisDatabaseManagerReadMode}
import JsonDefinitions.Author

object A03_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        Helpers.setInitialStartTimeMilliseconds();

        println("Amount of distinct authors:");
        println(QueryManager.distinctAuthors());
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Authors with the most articles:");
        val authorWithMostArticles: Author = QueryManager.mostArticles().head;
        println("Author with most articles: " + authorWithMostArticles);

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        RedisDatabaseManagerReadMode.closeConnection();

        println("Terminated.");
    }
}
