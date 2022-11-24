package Spark.Queries

import DataClasses.Author
import Spark.{ParquetReader, SparkConnectionManager}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, explode}

/*
Handles querying methods
 */
object QueryManagerSQL {

    ParquetReader.createOrReplaceArticlesView();

    def distinctAuthors(): Long = {

        // TODO
        return 0L;
    };

    def mostArticles(): List[Author] = {

        // TODO
        return List.empty;
    };

}
