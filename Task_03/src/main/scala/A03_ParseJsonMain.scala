import Additional.LoggingHelper
import Additional.LoggingHelper.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DataClasses.Article
import Spark.SparkConnectionManager
import org.apache.spark.sql.Dataset

object A03_ParseJsonMain {

    def main(args: Array[String]): Unit = {
        println("Starting Task_03...");
        LoggingHelper.setInitialStartTimeMilliseconds();

        println("Reading JSON file...");
        val articlesDataset: Dataset[Article] = SparkConnectionManager.readJsonFileIntoDataset();
        articlesDataset.createOrReplaceTempView("ArticlesFromJSON")

        println("Writing parquet file...");
        SparkConnectionManager.removeOldParquetDirectory();
        articlesDataset.write.parquet(SparkConnectionManager.PARQUET_SAVE_PATH);

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
