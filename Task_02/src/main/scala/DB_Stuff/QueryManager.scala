package DB_Stuff

import JsonDefinitions.{Article, Author}

object QueryManager {
    // TODO
    def titleByID(articleID: Long): String = ???;
    def authors(articleID: Long): List[Author] = ???;
    def articles(authorID: Long): List[Article] = ???;
    def referencedBy(articleID: Long): List[Article] = ???;

    // TODO
    def mostArticles(): List[Author] = ???;
    def distinctAuthorsExact(): Long = ???;
    def distinctAuthorsHyperLogLog(): Long = ???;
}
