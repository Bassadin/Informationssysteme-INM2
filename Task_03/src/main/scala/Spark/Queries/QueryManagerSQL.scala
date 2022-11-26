package Spark.Queries

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

        // TODO
        // https://stackoverflow.com/a/12235631/3526350
        val sqlString =
            """
              SELECT
                authors.*,
                COUNT(authors.id) AS value_occurrence
              FROM authors
              GROUP BY authors.id
              ORDER BY value_occurrence DESC
              LIMIT 1;
              """;

        val sqlString2 =
            """
                SELECT * FROM my_table GROUP BY value ORDER BY  count(*) DESC
            """;

        val sqlResultDataFrame = SparkConnectionManager.sparkSession.sql(sqlString);

        val authorsList: List[Author] = sqlResultDataFrame
            .collect()
            .map((eachRow: Row) => {
                Author(
                  eachRow.getAs[Long]("id"),
                  eachRow.getAs[String]("name"),
                  eachRow.getAs[String]("org")
                );
            })
            .toList;

        // TODO
        return authorsList;
    };

}
