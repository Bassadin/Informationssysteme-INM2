import Additional.LoggingHelper
import Additional.LoggingHelper.{getTimeDifferenceStringBetween, millisecondsTimeOnStart}
import DataClasses.Article
import org.apache.spark.sql.{Encoders, SparkSession}

object A03_ParseJsonMain {
    final val JSON_PATH: String = "./src/data/dblp.v12.json";
//    final val SPARK_MASTER_URL = "spark://localhost:7077";
    final val SPARK_MASTER_URL = "local[*]";

    def main(args: Array[String]): Unit = {
        // Measure time before starting as reference timeframe

        println(System.getProperty("java.version"));

        LoggingHelper.setInitialStartTimeMilliseconds();

        println("Starting Task_03...");

        // configure spark// configure spark
        val spark = SparkSession.builder
            .appName("Read JSON Articles File to DataSet")
            .master(SPARK_MASTER_URL)
            .config("spark.sql.caseSensitive", "true")
            .getOrCreate();

        // Java Bean (data class) used to apply schema to JSON data
        val articleEncoder = Encoders.bean(classOf[Article]);

        // read JSON file to Dataset
        val jsonDataSet = spark.read.json(JSON_PATH).as(articleEncoder);

        println(s"Total elapsed time: ${getTimeDifferenceStringBetween(millisecondsTimeOnStart)}");
        println("Terminated.");
    }
}
