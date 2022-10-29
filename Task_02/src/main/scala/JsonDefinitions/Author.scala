package JsonDefinitions

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Author(id: Long, name: String, org: Option[String]);

object AuthorProtocol extends DefaultJsonProtocol {
    implicit val authorFormat: RootJsonFormat[Author] = jsonFormat(
        Author,
        "id",
        "name",
        "org"
    )
}