import Additional.Helpers.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DB_Stuff.QueryManager

object A02_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        println("Title for article 1091:");
        println(QueryManager.titleByID(1091));

        println("Authors for article 1091:");
        println(QueryManager.authors(1091));

        println("Articles for author 2312688602:");
        println(QueryManager.articles(2312688602L));

        println("Articles referenced by article 2018037215:");
        println(QueryManager.referencedBy(2018037215L));

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
