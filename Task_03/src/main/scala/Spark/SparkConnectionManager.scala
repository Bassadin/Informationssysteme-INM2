package Spark

import Additional.LoggingHelper
import DataClasses.{Article, Author}
import org.apache.spark.sql.{Dataset, Encoder, Encoders, SparkSession}

import java.io.File
import scala.reflect.io.Directory

object SparkConnectionManager {
    final val JSON_PATH: String = "./src/data/dblp.v12.new.json";
    final val PARQUET_SAVE_PATH = "src/data/articles";

    //    final val SPARK_MASTER_URL = "spark://localhost:7077";
    final val SPARK_MASTER_URL = "local[*]";

    final val ARTICLE_SCHEMA = Encoders.product[Article].schema;

    // configure spark// configure spark
    val sparkSession: SparkSession = SparkSession.builder
        .appName("ArticlesFromJSON")
        .master(SPARK_MASTER_URL)
        .config("spark.sql.caseSensitive", "true")
        .getOrCreate();

    sparkSession.sparkContext.setLogLevel("ERROR");

    // Java Bean (data class) used to apply schema to JSON data
    val articleEncoder: Encoder[Article] = Encoders.bean(classOf[Article]);
//    val authorEncoder: Encoder[Author] = Encoders.bean(classOf[Author]);

    def readJsonFileIntoDataset(jsonPath: String = JSON_PATH): Dataset[Article] = {
        val articlesDataset = sparkSession.read
            .schema(ARTICLE_SCHEMA)
            .json(jsonPath)
            .as(articleEncoder);
        articlesDataset.printSchema();
        LoggingHelper.printElapsedTimeStatusMessage();

        return articlesDataset;
    }

    def removeOldParquetDirectory(): Unit = {
        val parquetDirectory = new Directory(new File(PARQUET_SAVE_PATH))
        parquetDirectory.deleteRecursively()
    }
}
