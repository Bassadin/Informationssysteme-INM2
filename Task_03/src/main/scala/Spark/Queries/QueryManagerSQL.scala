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

        authorsDataFrame.createOrReplaceTempView("distinctAuthors");

        val sqlString: String = "SELECT COUNT(id) as distinctAuthorsCount from distinctAuthors";
        val sqlResultDataFrame = SparkConnectionManager.sparkSession.sql(sqlString);

        val amountOfDistinctAuthors = sqlResultDataFrame.select("distinctAuthorsCount").collect().head.getLong(0);

        return amountOfDistinctAuthors;
    };

    def mostArticles(): List[Author] = {

        // TODO
        return List.empty;
    };

}
