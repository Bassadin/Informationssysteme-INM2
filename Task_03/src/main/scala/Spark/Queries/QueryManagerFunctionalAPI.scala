package Spark.Queries

import DataClasses.Author
import Spark.{ParquetReader, SparkConnectionManager}
import org.apache.spark.sql.DataFrame

/*
Handles querying methods
 */
object QueryManagerFunctionalAPI {

    ParquetReader.createOrReplaceArticlesView();

    def mostArticles(): List[Author] = ???;

    def distinctAuthors(): Long = ???;
}
