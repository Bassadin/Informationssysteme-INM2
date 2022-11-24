package Spark

import DataClasses.Author
import org.apache.spark.sql.DataFrame

/*
Handles querying methods
 */
object QueryManager {

    ParquetReader.createOrReplaceArticlesView();

    def mostArticles(): List[Author] = {
        val sqlString = "SELECT * FROM ArticlesFromJSON WHERE title LIKE 'Preliminary Desig%'";
        val sqlQuery: DataFrame = SparkConnectionManager.sparkSession.sql(sqlString);
        sqlQuery.show();

        ???
    };

    def distinctAuthors(): Long = ???;

    def articlesCountJSON(): Long = ???;

    def articlesCountParquet(): Long = ???;
}
