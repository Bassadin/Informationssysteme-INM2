package JsonDefinitions

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class Author(id: Long, name: String, org: String);

object AuthorProtocol extends DefaultJsonProtocol {
    implicit val authorFormat: RootJsonFormat[Author] = jsonFormat(
        Author,
        "id",
        "name",
        "org"
    )
}