import java.sql.DriverManager
import scala.io.Source

object Main {

    def main(args: Array[String]): Unit = {
        println("Starting...")

        val connection = DriverManager.getConnection("jdbc:h2:./demo");
        val statement = connection.createStatement();

        val createAuthors =
            """CREATE TABLE IF NOT EXISTS authors (
              author_id INT NOT NULL PRIMARY KEY,
              name VARCHAR(30) NOT NULL,
              org VARCHAR(30) NOT NULL
              );""".replaceAll("\n", " ");
        statement.execute(createAuthors);

        val createArticles =
            """CREATE TABLE IF NOT EXISTS articles (
              title VARCHAR(100) NOT NULL,
              article_id INT NOT NULL PRIMARY KEY,
              `year` INT NOT NULL,
              n_citation INT NOT NULL,
              page_start INT NOT NULL,
              page_end INT NOT NULL,
              doc_type VARCHAR(30) NOT NULL,
              publisher VARCHAR(30) NOT NULL,
              volume VARCHAR(30) NOT NULL,
              issue VARCHAR(30) NOT NULL,
              doi VARCHAR(30) NOT NULL
              );""".replaceAll("\n", " ");
        statement.execute(createArticles);

        val createReferences =
            """CREATE TABLE IF NOT EXISTS references (
              reference_id INT PRIMARY KEY NOT NULL
              );""".replaceAll("\n", " ");
        statement.execute(createReferences);

        val createArticlesReferences =
            """CREATE TABLE IF NOT EXISTS articles_references (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              article_from_id INT NOT NULL,
              FOREIGN KEY (article_from_id) REFERENCES articles(article_id),
              article_to_id INT NOT NULL,
              FOREIGN KEY (article_to_id) REFERENCES articles(article_id),
              CHECK (article_from_id!=article_to_id)
              );""".replaceAll("\n", " ");
        statement.execute(createArticlesReferences);

        statement.close();

        val fileLines = Source.fromFile("./src/data/dblp.v12.json").getLines();

        println(fileLines.next());

        connection.close();
        println("Terminated.");
    }
}