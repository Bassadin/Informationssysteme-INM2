import Additional.Helpers
import Additional.Helpers.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DB_Stuff.QueryManager
import JsonDefinitions.Author

object A02_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        Helpers.setInitialStartTimeMilliseconds();

        println("Title for article 1091:");
        println(QueryManager.titleByID(1091));
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Authors for article 1091:");
        println(QueryManager.authors(1091));
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Articles for author 2312688602:");
        println(QueryManager.articles(2312688602L));
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Articles referenced by article 2018037215:");
        println(QueryManager.referencedBy(2018037215L));
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Exact amount of distinct authors:");
        println(QueryManager.distinctAuthorsExact());
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Amount of distinct authors with HyperLogLog:");
        println(QueryManager.distinctAuthorsHyperLogLog());
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println("Authors with the most articles:");
        val authorWithMostArticles: Author = QueryManager.mostArticles().head;
        println(authorWithMostArticles);
        println("Amount: " + QueryManager.articles(authorWithMostArticles.id).length);
        println(s"Elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
