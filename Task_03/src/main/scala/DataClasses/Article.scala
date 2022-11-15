package DataClasses

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
    references: Option[List[Long]]
);
