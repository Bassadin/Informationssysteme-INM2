import java.sql.DriverManager
import scala.io.Source

object Main {

    def main(args: Array[String]): Unit = {
        println("Starting...")

        val connection = DriverManager.getConnection("jdbc:h2:./demo");
        val statement = connection.createStatement();

        val create = "CREATE TABLE IF NOT EXISTS reihen (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20), baende INT);";
        statement.execute(create);

        val insert = "INSERT INTO reihen (name, baende) VALUES ('hallo', 5);";
        statement.execute(insert);

        statement.close();

        val fileLines = Source.fromFile("./src/data/dblp.v12.json").getLines();

        println(fileLines.next());

        connection.close();
        println("Terminated.");
    }
}