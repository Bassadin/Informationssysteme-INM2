package Spark.Queries

import DataClasses.Author
import Spark.ParquetReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, explode}

/*
Handles querying methods
 */
object QueryManagerFunctionalAPI {

    ParquetReader.createOrReplaceArticlesView();

    def distinctAuthors(): Long = {
        val authorsDataFrame: DataFrame = ParquetReader.parquetFileDataFrame
            .select(explode(col("authors")))
            .select("col.id");
        val distinctAuthorsAmount: Long = authorsDataFrame.distinct().count();

        return distinctAuthorsAmount;
    };

    def mostArticles(): List[Author] = {
        // TODO
        return List.empty;
    };

}
