package Spark

import org.apache.spark.sql.DataFrame

object ParquetReader {
    println("Loading parquet file...");

    val parquetFileDataFrame: DataFrame = SparkConnectionManager.sparkSession.read
        .schema(SparkConnectionManager.ARTICLE_SCHEMA)
        .parquet(SparkConnectionManager.PARQUET_SAVE_PATH);

    println("Creating view with parquet file data...");

    def createOrReplaceArticlesView(): Unit = {
        parquetFileDataFrame.createOrReplaceTempView("ArticlesFromJSON");
    }
}
