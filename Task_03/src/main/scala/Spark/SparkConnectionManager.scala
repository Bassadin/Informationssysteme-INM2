package Spark

import Additional.LoggingHelper
import DataClasses.Article
import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}

object SparkConnectionManager {
    final val JSON_PATH: String = "./src/data/dblp.v12.json";
    final val PARQUET_SAVE_PATH = "src/data/articles";

    //    final val SPARK_MASTER_URL = "spark://localhost:7077";
    final val SPARK_MASTER_URL = "local[*]";

    // configure spark// configure spark
    val sparkSession: SparkSession = SparkSession.builder
        .appName("ArticlesFromJSON")
        .master(SPARK_MASTER_URL)
        .config("spark.sql.caseSensitive", "true")
        .getOrCreate();

    // Java Bean (data class) used to apply schema to JSON data
    val articleEncoder: Encoder[Article] = Encoders.bean(classOf[Article]);

    def readJsonFileIntoDataset(jsonPath: String = JSON_PATH): Dataset[Article] = {
        val articlesDataset = sparkSession.read.json(jsonPath).as(articleEncoder);
        LoggingHelper.printElapsedTimeStatusMessage();

        return articlesDataset;
    }
}
