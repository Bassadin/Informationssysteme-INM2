import java.sql.DriverManager


object DatabaseManager {
    val dbConnection = DriverManager.getConnection("jdbc:h2:./demo");

    def createDatabases: Unit = {
        val createDBTablesStatement = dbConnection.createStatement();
        val createAuthors =
            """CREATE TABLE IF NOT EXISTS authors (
              author_id INT NOT NULL PRIMARY KEY,
              name VARCHAR(30) NOT NULL,
              org VARCHAR(30) NOT NULL
              );""";
        createDBTablesStatement.execute(createAuthors);

        val createArticles =
            """CREATE TABLE IF NOT EXISTS articles (
              title VARCHAR(100) NOT NULL,
              article_id INT NOT NULL PRIMARY KEY,
              `year` INT NOT NULL,
              n_citation INT NOT NULL,
              page_start INT NOT NULL,
              page_end INT NOT NULL,
              doc_type VARCHAR(30),
              publisher VARCHAR(30) NOT NULL,
              volume VARCHAR(30) NOT NULL,
              issue VARCHAR(30) NOT NULL,
              doi VARCHAR(30) NOT NULL
              );""";
        createDBTablesStatement.execute(createArticles);

        val createReferences =
            """CREATE TABLE IF NOT EXISTS references (
              reference_id INT PRIMARY KEY NOT NULL
              );""";
        createDBTablesStatement.execute(createReferences);

        val createArticlesReferences =
            """CREATE TABLE IF NOT EXISTS articles_references (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              article_from_id INT NOT NULL,
              FOREIGN KEY (article_from_id) REFERENCES articles(article_id),
              article_to_id INT NOT NULL,
              FOREIGN KEY (article_to_id) REFERENCES articles(article_id),
              CHECK (article_from_id!=article_to_id)
              );""";
        createDBTablesStatement.execute(createArticlesReferences);

        val createArticlesAuthors =
            """CREATE TABLE IF NOT EXISTS articles_authors (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              article_id INT NOT NULL,
              FOREIGN KEY (article_id) REFERENCES articles(article_id),
              author_id INT NOT NULL,
              FOREIGN KEY (author_id) REFERENCES authors(author_id)
              );""";
        createDBTablesStatement.execute(createArticlesAuthors);

        createDBTablesStatement.close();
    }

    def closeConnection: Unit = {
        dbConnection.close;
    }
}
