package Spark.Queries

import Spark.ParquetReader

/*
Handles querying methods
 */
object QueryManagerArticleCounts {

    ParquetReader.createOrReplaceArticlesView();

    def articlesCountJSON(): Long = ???;

    def articlesCountParquet(): Long = ???;
}
