package DB_Stuff

import JsonDefinitions.{Article, Author}

object QueryManager {
    // TODO
    def titleByID(articleID: Long): String = null;
    def authors(articleID: Long): List[Author] = null;
    def articles(authorID: Long): List[Article] = null;
    def referencedBy(articleID: Long): List[Article] = null;

    // TODO
    def mostArticles(): List[Author] = null;
    def distinctAuthors(): Long = null;
}
