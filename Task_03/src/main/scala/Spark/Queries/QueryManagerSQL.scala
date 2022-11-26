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

        // TODO
        // https://stackoverflow.com/a/12235631/3526350
        val sqlString =
            """
              SELECT
                authors.*,
                COUNT(authors.id) AS value_occurrence
              FROM authors
              GROUP BY authors.id, authors.name, authors.org
              ORDER BY value_occurrence DESC
              LIMIT 1;
              """;

        val sqlString2 =
            """
                SELECT
                    *,
                    COUNT(authors.id) AS article_count
                FROM authors
                WHERE article_count = (SELECT MAX(COUNT(authors.id)) FROM authors)
                ORDER BY authors.id ASC
            """;
        val sqlResultDataFrame = SparkConnectionManager.sparkSession.sql(sqlString);

        val resultData = sqlResultDataFrame.select("authors.id", "authors.name", "authors.org");
        val authorsList = resultData.as[Author](SparkConnectionManager.authorEncoder).collect();

        // TODO
        return authorsList.toList;
    };

}
