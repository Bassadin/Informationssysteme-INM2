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

        return authorsDataFrame.distinct().count();
    };

    def mostArticles(): List[Author] = {
        val authorsDataFrame: DataFrame = ParquetReader.parquetFileDataFrame
            .select(explode(col("authors")))
            .select("col.*")
            .as("a1");

        val rankDataFrame = authorsDataFrame
            .groupBy(col("id"))
            .agg(count("id").alias("count"))
            .select(
              col("id"),
              rank().over(Window.orderBy(col("count").desc)).as("article_count_rank")
            )
            .as("a2");

        val sqlResultDataFrame = authorsDataFrame
            .join(rankDataFrame, authorsDataFrame("id") === rankDataFrame("id"))
            .where(col("article_count_rank") === 1)
            .groupBy("a1.id")
            .agg(
              first("a1.id").as("id"),
              first("name", ignoreNulls = true).as("name"),
              first("org", ignoreNulls = true).as("org")
            );

        return sqlResultDataFrame.collect().map(RowConversion.rowToAuthor).toList;
    };
}
