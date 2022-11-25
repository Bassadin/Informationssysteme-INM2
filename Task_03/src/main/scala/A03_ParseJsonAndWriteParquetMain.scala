import Additional.LoggingHelper
import DataClasses.Article
import Spark.SparkConnectionManager
import org.apache.spark.sql.Dataset

object A03_ParseJsonAndWriteParquetMain {

    def main(args: Array[String]): Unit = {
        println("Starting Task_03...");
        LoggingHelper.setStartTimeMilliseconds();

        println("Reading JSON file...");
        val articlesDataset: Dataset[Article] = SparkConnectionManager.readJsonFileIntoDataset();
        articlesDataset.createOrReplaceTempView("ArticlesFromJSON")

        println("Writing parquet file...");
        SparkConnectionManager.removeOldParquetDirectory();
        articlesDataset.write.parquet(SparkConnectionManager.PARQUET_SAVE_PATH);

        LoggingHelper.printElapsedTimeStatusMessage();
        println("Terminated.");
    }
}
