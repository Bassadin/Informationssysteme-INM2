import java.sql.DriverManager
import scala.io.Source

object Main {

    def main(args: Array[String]): Unit = {
        println("Starting...")

        DatabaseManager.createDatabases;

        // JSON stuff
        val fileLines = Source.fromFile("./src/data/dblp.v12.json").getLines();

        fileLines.take(500).foreach(eachLineString => {
            println(eachLineString);
        });

        DatabaseManager.closeConnection;
        println("Terminated.");
    }
}