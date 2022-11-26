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

        // https://stackoverflow.com/a/39816161/3526350
        val sqlString =
            """
                SELECT
                    a1.*
              FROM authors a1
              RIGHT JOIN
                (
                    SELECT max(id) as id, COUNT(id) AS article_count
                    FROM authors
                    GROUP BY id
                    ORDER BY article_count DESC
                    LIMIT 1
                ) a2 ON a1.id = a2.id
            """;

        val sqlResultDataFrame = SparkConnectionManager.sparkSession.sql(sqlString);

        val authorsList: List[Author] = sqlResultDataFrame
            .collect()
            .map(RowConversion.rowToAuthor)
            .toList;

        // TODO
        return authorsList;
    };

}
