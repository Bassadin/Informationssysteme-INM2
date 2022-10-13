import scala.io.Source

object Main {

    final val JSON_PATH: String = "./src/data/dblp.v12.json";

    def main(args: Array[String]): Unit = {
        println("Starting...")

        DatabaseManager.createDatabases;

        // JSON stuff
        val fileLines = Source.fromFile(JSON_PATH).getLines();

        fileLines.take(500).foreach(eachLineString => {
            println(eachLineString);
        });

        DatabaseManager.closeConnection;
        println("Terminated.");
    }
}