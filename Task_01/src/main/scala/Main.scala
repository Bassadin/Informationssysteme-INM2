import scala.io.Source

import spray.json._
import DefaultJsonProtocol._

object Main {

    final val JSON_PATH: String = "./src/data/dblp.v12.json";

    def main(args: Array[String]): Unit = {
        println("Starting...")

        DatabaseManager.createDatabases;

        // JSON stuff
        val fileLines = Source.fromFile(JSON_PATH).getLines();

        // Skip first line, it only contains a []
        fileLines.next();

        fileLines.take(500).foreach(eachLineString => {

            val cleanedLineString = eachLineString.replaceFirst("^,", "");
            //            println(cleanedLineString);

            val parsedJsonObject = cleanedLineString.parseJson.asJsObject;

            println(parsedJsonObject.getFields("authors"));
        });

        DatabaseManager.closeConnection;
        println("Terminated.");
    }
}