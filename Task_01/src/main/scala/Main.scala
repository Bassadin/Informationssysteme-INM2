import scala.io.Source

import spray.json._

case class Author(id: Long, name: String, org: Option[String]);

case class Article(id: Long, authors: List[Author]);

object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val authorFormat = jsonFormat(Author, "id", "name", "org")
    implicit val articleFormat = jsonFormat(Article, "id", "authors")
}

object Main {

    import MyJsonProtocol._

    final val JSON_PATH: String = "./src/data/dblp.v12.json";


    def main(args: Array[String]): Unit = {
        println("Starting...")
        DatabaseManager.createDatabases;

        // JSON stuff
        val fileLines = Source.fromFile(JSON_PATH).getLines();

        // Skip first line, it only contains a []
        fileLines.next();
        fileLines.take(500).foreach(eachLineString => {
            val cleanedLineString: String = eachLineString.replaceFirst("^,", "");
            val parsedArticle: Article = cleanedLineString.parseJson.convertTo[Article];

            parsedArticle.authors.foreach(eachAuthor => {
                DatabaseManager.addAuthor(eachAuthor);
            })

            println(parsedArticle);
        });

        DatabaseManager.closeConnection;
        println("Terminated.");
    }
}