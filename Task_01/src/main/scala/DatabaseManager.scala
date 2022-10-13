
import java.sql.{DriverManager, PreparedStatement}


object DatabaseManager {
    val dbConnection = DriverManager.getConnection("jdbc:h2:./demo");

    def createDatabases: Unit = {
        val createDBTablesStatement = dbConnection.createStatement();
        val createAuthorsSqlString =
            """CREATE TABLE IF NOT EXISTS authors (
              author_id BIGINT NOT NULL PRIMARY KEY,
              name VARCHAR(200) NOT NULL,
              org VARCHAR(500)
              );""";
        createDBTablesStatement.execute(createAuthorsSqlString);

        val createArticlesSqlString =
            """CREATE TABLE IF NOT EXISTS articles (
              article_id BIGINT NOT NULL PRIMARY KEY,
              title VARCHAR(500) NOT NULL,
              `year` INT NOT NULL,
              n_citation INT NOT NULL,
              page_start VARCHAR(20) NOT NULL,
              page_end VARCHAR(20) NOT NULL,
              doc_type VARCHAR(30),
              publisher VARCHAR(500) NOT NULL,
              volume VARCHAR(30) NOT NULL,
              issue VARCHAR(30) NOT NULL,
              doi VARCHAR(40)
              );""";
        createDBTablesStatement.execute(createArticlesSqlString);

        val createReferencesSqlString =
            """CREATE TABLE IF NOT EXISTS references (
              reference_id INT PRIMARY KEY NOT NULL
              );""";
        createDBTablesStatement.execute(createReferencesSqlString);

        val createArticlesReferencesSqlString =
            """CREATE TABLE IF NOT EXISTS articles_references (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              article_from_id INT NOT NULL,
              FOREIGN KEY (article_from_id) REFERENCES articles(article_id),
              article_to_id INT NOT NULL,
              FOREIGN KEY (article_to_id) REFERENCES articles(article_id),
              CHECK (article_from_id!=article_to_id)
              );""";
        createDBTablesStatement.execute(createArticlesReferencesSqlString);

        val createArticlesAuthorsSqlString =
            """CREATE TABLE IF NOT EXISTS articles_authors (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              article_id INT NOT NULL,
              FOREIGN KEY (article_id) REFERENCES articles(article_id),
              author_id INT NOT NULL,
              FOREIGN KEY (author_id) REFERENCES authors(author_id)
              );""";
        createDBTablesStatement.execute(createArticlesAuthorsSqlString);

        createDBTablesStatement.close();

    }

    def closeConnection: Unit = {
        dbConnection.close;
    }

    def addAuthor(author: Author): Unit = {
        // TODO !!!!!!
        val authorInsertStatement = dbConnection.prepareStatement("MERGE INTO authors VALUES (?, ?, ?)");

        authorInsertStatement.setLong(1, author.id);
        authorInsertStatement.setString(2, author.name);
        if (author.org.isDefined) {
            authorInsertStatement.setString(3, author.org.get);
        } else {
            authorInsertStatement.setNull(3, 0);
        }
        authorInsertStatement.executeUpdate();
    }

    def addArticle(articleToAdd: Article) = {
        // TODO !!!!!!
        val authorInsertStatement = dbConnection.prepareStatement("MERGE INTO articles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        authorInsertStatement.setLong(1, articleToAdd.id);
        authorInsertStatement.setString(2, articleToAdd.title);
        authorInsertStatement.setInt(3, articleToAdd.year);
        authorInsertStatement.setInt(4, articleToAdd.n_citation);
        authorInsertStatement.setString(5, articleToAdd.page_start);
        authorInsertStatement.setString(6, articleToAdd.page_end);
        if (articleToAdd.doc_type.isDefined) {
            authorInsertStatement.setString(7, articleToAdd.doc_type.get);
        } else {
            authorInsertStatement.setNull(7, 0);
        }
        authorInsertStatement.setString(8, articleToAdd.publisher);
        authorInsertStatement.setString(9, articleToAdd.volume);
        authorInsertStatement.setString(10, articleToAdd.issue);

        if (articleToAdd.DOI.isDefined) {
            authorInsertStatement.setString(11, articleToAdd.DOI.get);
        } else {
            authorInsertStatement.setNull(11, 0);
        }

        authorInsertStatement.executeUpdate();
    }
}
