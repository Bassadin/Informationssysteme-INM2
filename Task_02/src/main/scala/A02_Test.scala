import Additional.Helpers.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DB_Stuff.QueryManager

object A02_Test {
    def main(args: Array[String]): Unit = {
        println("Starting...");

        println(QueryManager.titleByID(2739457533L));
        println(QueryManager.authors(2739457533L));

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
