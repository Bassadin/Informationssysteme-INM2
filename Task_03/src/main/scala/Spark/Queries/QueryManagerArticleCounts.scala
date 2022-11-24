package Spark.Queries

import Spark.{ParquetReader, SparkConnectionManager}

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

    def articlesCountParquet(): Long = ???;
}
