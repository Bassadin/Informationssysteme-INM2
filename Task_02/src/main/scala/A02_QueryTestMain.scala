import Additional.Helpers.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DB_Stuff.QueryManager

object A02_QueryTestMain {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        println(QueryManager.titleByID(1091));
        println(QueryManager.authors(1091));

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
