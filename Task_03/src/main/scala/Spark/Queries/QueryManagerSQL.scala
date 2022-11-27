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

        authorsDataFrame.createOrReplaceTempView("authors");

        val sqlResultDataFrame = SparkConnectionManager.sparkSession
            .sql("SELECT DISTINCT COUNT(id) as distinctAuthorsCount from authors");

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
        authorsDataFrame.createOrReplaceTempView("authors");

        // https://sql-bits.com/how-to-find-the-mode-in-sql/
        val sqlString =
            """
                SELECT *
                FROM authors a1
                JOIN (
                    SELECT
                            authors.id,
                            RANK() OVER (ORDER BY COUNT(*) DESC) AS article_count_rank
                        FROM authors
                        GROUP BY authors.id
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
