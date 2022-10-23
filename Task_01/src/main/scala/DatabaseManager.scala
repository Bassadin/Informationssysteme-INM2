import Helpers.getCurrentTimeStringFrom
import JsonDefinitions.{Article, Author}

import java.io.File
import java.sql.DriverManager

object DatabaseManager {
    val DB_PATH = "./demo.mv.db"

    // Delete old DB file
    println("Deleting old db...");
    new File(DB_PATH).delete();

    val dbConnection = DriverManager.getConnection("jdbc:h2:./demo");

    // Call this right away so that the databases are initialized for the prepared statements later
    this.createDatabases;

    def createDatabases: Unit = {
        val createDBTablesStatement = dbConnection.createStatement();
        val createAuthorsSqlString =
            """
              CREATE TABLE IF NOT EXISTS authors (
              author_id BIGINT NOT NULL PRIMARY KEY,
              name VARCHAR(200) NOT NULL,
              org VARCHAR(500)
              );
            """;
        createDBTablesStatement.execute(createAuthorsSqlString);

        val createArticlesSqlString =
            """
              CREATE TABLE IF NOT EXISTS articles (
              article_id BIGINT NOT NULL PRIMARY KEY,
              title VARCHAR(800) NOT NULL,
              `year` INT NOT NULL,
              n_citation INT NOT NULL,
              page_start VARCHAR(20) NOT NULL,
              page_end VARCHAR(20) NOT NULL,
              doc_type VARCHAR(30) ,
              publisher VARCHAR(500) NOT NULL,
              volume VARCHAR(30) NOT NULL,
              issue VARCHAR(30) NOT NULL,
              doi VARCHAR(40)
              );
            """;
        createDBTablesStatement.execute(createArticlesSqlString);

        val createArticlesReferencesSqlString =
            """
              CREATE TABLE IF NOT EXISTS articles_references (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              referencing_article_id BIGINT NOT NULL ,
              referenced_article_id BIGINT NOT NULL,
              FOREIGN KEY (referencing_article_id) REFERENCES articles(article_id),
              CHECK (referencing_article_id!=referenced_article_id)
              );
            """;
        createDBTablesStatement.execute(createArticlesReferencesSqlString);

        val createArticlesAuthorsSqlString =
            """
              CREATE TABLE IF NOT EXISTS articles_authors (
              id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
              article_id BIGINT NOT NULL,
              author_id BIGINT NOT NULL,
              FOREIGN KEY (article_id) REFERENCES articles(article_id),
              FOREIGN KEY (author_id) REFERENCES authors(author_id)
              );
            """;
        createDBTablesStatement.execute(createArticlesAuthorsSqlString);

        createDBTablesStatement.close();
    }

    def closeConnection: Unit = {
        dbConnection.close;
    }

    // JsonDefinitions.Article relationships
    val articleRelationInsertStatement = dbConnection.prepareStatement(
      "INSERT INTO articles_references (referencing_article_id, referenced_article_id) VALUES (?, ?)"
    );

    def addArticleToArticleRelation(referencingArticle: Article, referencedArticleId: Long): Unit = {
        articleRelationInsertStatement.setLong(1, referencingArticle.id);
        articleRelationInsertStatement.setLong(2, referencedArticleId);

        articleRelationInsertStatement.executeUpdate();
    }

    def addArticleToArticlesRelation(referencingArticle: Article, referencedArticles: List[Long]): Unit = {
        referencedArticles.foreach(eachArticle => this.addArticleToArticleRelation(referencingArticle, eachArticle))
    }

    // JsonDefinitions.Author relationships
    val authorRelationInsertStatement = dbConnection.prepareStatement(
      "INSERT INTO articles_authors (article_id, author_id) VALUES (?, ?)"
    );

    def addArticleToAuthorRelation(article: Article, author: Author): Unit = {
        authorRelationInsertStatement.setLong(1, article.id);
        authorRelationInsertStatement.setLong(2, author.id);

        authorRelationInsertStatement.executeUpdate();
    }

    def addArticleToAuthorsRelation(
        article: Article,
        authors: List[Author]
    ): Unit = {
        authors.foreach(eachAuthor => this.addArticleToAuthorRelation(article, eachAuthor));
    }

    // Authors
    val authorInsertStatement =
        dbConnection.prepareStatement("MERGE INTO authors VALUES (?, ?, ?)");

    def addAuthor(authorToAdd: Author): Unit = {
        authorInsertStatement.setLong(1, authorToAdd.id);
        authorInsertStatement.setString(2, authorToAdd.name);

        authorToAdd.org match {
            case Some(i) => authorInsertStatement.setString(3, i)
            case None    => authorInsertStatement.setNull(3, 0)
        }

        authorInsertStatement.executeUpdate();
    }

    def addAuthors(authorsToAdd: List[Author]): Unit = {
        authorsToAdd.foreach(eachAuthor => this.addAuthor(eachAuthor))
    }

    // Articles
    val articleInsertStatement = dbConnection.prepareStatement(
      "MERGE INTO articles VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
    );

    def addArticle(articleToAdd: Article) = {

        articleInsertStatement.setLong(1, articleToAdd.id);
        articleInsertStatement.setString(2, articleToAdd.title);
        articleInsertStatement.setInt(3, articleToAdd.year);
        articleInsertStatement.setInt(4, articleToAdd.n_citation);
        articleInsertStatement.setString(5, articleToAdd.page_start);
        articleInsertStatement.setString(6, articleToAdd.page_end);

        articleToAdd.doc_type match {
            case Some(i) => articleInsertStatement.setString(7, i)
            case None    => articleInsertStatement.setNull(7, 0)
        }

        articleInsertStatement.setString(8, articleToAdd.publisher);
        articleInsertStatement.setString(9, articleToAdd.volume);
        articleInsertStatement.setString(10, articleToAdd.issue);

        articleToAdd.DOI match {
            case Some(i) => articleInsertStatement.setString(11, articleToAdd.DOI.get);
            case None    => articleInsertStatement.setNull(11, 0);
        }

        articleInsertStatement.executeUpdate();
    }

    def enableArticleRefsForeignKeyCheck(): Unit = {
        val timeBeforeFKEnabling = System.currentTimeMillis();
        println("Enabling FK checks...");
        val alterForeignKeyStatement = dbConnection.createStatement();

        alterForeignKeyStatement.execute("""
              ALTER TABLE articles_references
              ADD FOREIGN KEY (referenced_article_id) REFERENCES articles(article_id);
            """);
        alterForeignKeyStatement.close();

        println(s"Enabling FK checks finished in ${getCurrentTimeStringFrom(timeBeforeFKEnabling)}.");
    }
}
