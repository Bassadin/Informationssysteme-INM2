import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Author(id: Long, name: String, org: Option[String]);

case class Article(
                      id: Long,
                      authors: Option[List[Author]],
                      title: String,
                      year: Int,
                      n_citation: Int,
                      page_start: String,
                      page_end: String,
                      doc_type: Option[String],
                      publisher: String,
                      volume: String,
                      issue: String,
                      DOI: Option[String],
                      references: Option[List[Long]]);

object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val authorFormat: RootJsonFormat[Author] = jsonFormat(
        Author,
        "id",
        "name",
        "org"
    )
    implicit val articleFormat: RootJsonFormat[Article] = jsonFormat(
        Article,
        "id",
        "authors",
        "title",
        "year",
        "n_citation",
        "page_start",
        "page_end",
        "doc_type",
        "publisher",
        "volume",
        "issue",
        "DOI",
        "references"
    )
}