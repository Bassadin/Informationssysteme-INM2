import java.sql.DriverManager

object Main {

    def main(args: Array[String]): Unit = {
        println("Starting...")

        val connection = DriverManager.getConnection("jdbc:h2:./demo");
        val statement = connection.createStatement();
        val create = "CREATE TABLE reihen (id INT PRIMARY KEY, name VARCHAR(20), baende INT)";
        statement.execute(create);
        statement.close();
        connection.close();

        println("Terminated.");
    }
}