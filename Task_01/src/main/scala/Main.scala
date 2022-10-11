object Main {
    case class Person private(firstName: String, lastName: String) {}

    def main(args: Array[String]): Unit = {
        val testPerson = Person("Donald", "Duck");

        println(testPerson);
    }
}