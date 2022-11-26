package Additional

import DataClasses.Author
import org.apache.spark.sql.Row

object RowConversion {
    def rowToAuthor(inputRow: Row): Author = {
        return Author(
          inputRow.getAs[Long]("id"),
          inputRow.getAs[String]("name"),
          inputRow.getAs[String]("org")
        )

    }
}
