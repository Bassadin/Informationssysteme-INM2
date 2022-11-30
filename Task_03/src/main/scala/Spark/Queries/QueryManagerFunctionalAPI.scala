package Spark.Queries

import Additional.RowConversion
import DataClasses.Author
import Spark.ParquetReader
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

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
        val authorsDataFrame: DataFrame = ParquetReader.parquetFileDataFrame
            .select(explode(col("authors")))
            .select("col.*");
        authorsDataFrame.createOrReplaceTempView("authors");

        val rankDataFrame = authorsDataFrame
            .groupBy(col("id"))
            .agg(count("id").alias("count"))
            .select(
              col("id"),
              rank()
                  .over(Window.orderBy(col("count").desc))
                  .as("article_count_rank")
            );

        val sqlResultDataFrame = authorsDataFrame
            .select("*")
            .join(rankDataFrame, authorsDataFrame("id") === rankDataFrame("id"))
            .where(rankDataFrame("article_count_rank") === 1);

        val authorsList: List[Author] = sqlResultDataFrame
            .collect()
            .map(RowConversion.rowToAuthor)
            .toList;

        return authorsList;
    };

}
