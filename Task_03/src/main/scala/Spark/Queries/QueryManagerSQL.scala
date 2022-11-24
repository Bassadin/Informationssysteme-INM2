package Spark.Queries

import DataClasses.Author
import Spark.{ParquetReader, SparkConnectionManager}
import org.apache.spark.sql.DataFrame

/*
Handles querying methods
 */
object QueryManagerSQL {

    ParquetReader.createOrReplaceArticlesView();

    def mostArticles(): List[Author] = {
        val sqlString = "SELECT * FROM ArticlesFromJSON WHERE title LIKE 'Preliminary Desig%'";
        val sqlQuery: DataFrame = SparkConnectionManager.sparkSession.sql(sqlString);
        sqlQuery.show();

        ???
    };

    def distinctAuthors(): Long = ???;


}
