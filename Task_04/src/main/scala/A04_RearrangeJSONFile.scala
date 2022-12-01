import java.io.FileWriter
import scala.io.Source
import scala.util.matching.Regex

object A04_RearrangeJSONFile {
    final private val INPUT_PATH = "src/data/dblp.v12.json";
    final private val OUTPUT_PATH = "src/data/dblp.v12.new.json";

    def main(args: Array[String]): Unit = {
        println("Starting rearranging JSON file...");

        val outputFileWriter = new FileWriter(OUTPUT_PATH);

        val jsonFileSource = Source.fromFile(INPUT_PATH);
        val jsonFileLinesIterator = jsonFileSource.getLines;

        jsonFileLinesIterator.next();

        // Use zipWithIndex to get an index iterator alongside the elements
        jsonFileLinesIterator.zipWithIndex.foreach { case (eachLineString, eachIndex) =>
            if (!eachLineString.equals("]")) {
                // Skip last line
                val cleanedLineString = eachLineString
                    .replace("\uFFFF", "?")
                    .replaceFirst("^,", "");

                val replacementRegex: Regex = """(?:"fos":\[.*?],|"indexed_abstract":\{.+?},|"venue":\{.*?},)""".r;
                val rearrangedLineString = replacementRegex.replaceAllIn(cleanedLineString, "");

                outputFileWriter.write(rearrangedLineString + "\n");

                if (eachIndex % 250_000 == 0) {
                    println(s"Parsed line $eachIndex");
                }
            }

        };

        println("Closing file writer...");

        outputFileWriter.close();

        println("Terminated.");
    }
}
