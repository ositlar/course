import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.jsoup.Jsoup
import org.litote.kmongo.getCollection
import org.jsoup.Jsoup.*
import java.io.FileReader
import java.io.Reader
import javax.xml.stream.XMLInputFactory

fun main() {
    val route = "C:/Users/Максим/Desktop/course/src/main/resources/35.html"
    val file = FileReader(route).readText()
    val htmlTable = Jsoup.parse(file)
    //val raws = htmlTable.select("table").select("tr").select("td").size.toString()
    //println(raws)
    val controller = htmlController(htmlTable)

    val mongo = mongoDatabase.getCollection<Group>().apply { drop() }
    mongo.insertMany(controller.groups)
    prettyPrintCursor(mongo.find())

    ////////////////////test///////////////////
}
