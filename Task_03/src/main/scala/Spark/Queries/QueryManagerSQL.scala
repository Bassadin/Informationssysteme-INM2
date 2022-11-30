package Spark.Queries

import Additional.RowConversion
import DataClasses.Author
import Spark.{ParquetReader, SparkConnectionManager}
import org.apache.spark.sql.functions.{col, explode}
import org.apache.spark.sql.{DataFrame, Row}

/*
Handles querying methods
 */
object QueryManagerSQL {

    ParquetReader.createOrReplaceArticlesView();

    def distinctAuthors(): Long = {
        val authorsDataFrame: DataFrame = ParquetReader.parquetFileDataFrame
            .select(explode(col("authors")))
            .select("col.id");

        authorsDataFrame.createOrReplaceTempView("authors_for_distinct");

        val sqlResultDataFrame = SparkConnectionManager.sparkSession
            .sql("SELECT COUNT(DISTINCT id) as distinctAuthorsCount from authors_for_distinct");

        val amountOfDistinctAuthors = sqlResultDataFrame
            .select("distinctAuthorsCount")
            .collect()
            .head
            .getLong(0);

        return amountOfDistinctAuthors;
    };

    def mostArticles(): List[Author] = {
        val authorsDataFrame: DataFrame = ParquetReader.parquetFileDataFrame
            .select(explode(col("authors")))
            .select("col.*");
        authorsDataFrame.createOrReplaceTempView("authors_for_most_articles");

        // https://sql-bits.com/how-to-find-the-mode-in-sql/
        val sqlString =
            """
                SELECT *
                FROM authors_for_most_articles a1
                JOIN (
                    SELECT
                            authors_for_most_articles.id,
                            RANK() OVER (ORDER BY COUNT(*) DESC) AS article_count_rank
                        FROM authors_for_most_articles
                        GROUP BY authors_for_most_articles.id
                ) a2
                ON a1.id = a2.id
                WHERE article_count_rank = 1;
            """;

        val sqlResultDataFrame = SparkConnectionManager.sparkSession.sql(sqlString);

        val authorsList: List[Author] = sqlResultDataFrame
            .collect()
            .map(RowConversion.rowToAuthor)
            .toList;

        return authorsList;
    };

}
