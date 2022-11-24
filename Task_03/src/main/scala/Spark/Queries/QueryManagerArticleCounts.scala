package Spark.Queries

import Spark.{ParquetReader, SparkConnectionManager}
import org.apache.spark.sql.DataFrame

import scala.io.Source

/*
Handles querying methods
 */
object QueryManagerArticleCounts {

    ParquetReader.createOrReplaceArticlesView();

    def articlesCountJSON(): Long = {
        val fileSource = Source.fromFile(SparkConnectionManager.JSON_PATH);
        val fileLinesAmount = fileSource.getLines.size
        fileSource.close()

        return fileLinesAmount;
    };

    def articlesCountParquet(): Long = {
        ParquetReader.createOrReplaceArticlesView();

        val sqlString = "SELECT COUNT(*) as articles_count FROM ArticlesFromJSON";
        val sqlResultDataFrame: DataFrame = SparkConnectionManager.sparkSession.sql(sqlString);

        return sqlResultDataFrame.collect()(0)(0).asInstanceOf[Long];
    };
}
