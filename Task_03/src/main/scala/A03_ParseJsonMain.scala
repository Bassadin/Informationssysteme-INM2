import Additional.LoggingHelper
import Additional.LoggingHelper.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DataClasses.Article
import Spark.SparkConnectionManager
import org.apache.spark.sql.{Encoders, SparkSession}

object A03_ParseJsonMain {


    def main(args: Array[String]): Unit = {
        println("Starting Task_03...");
        LoggingHelper.setInitialStartTimeMilliseconds();


        SparkConnectionManager.readJsonFileIntoDataset();

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
