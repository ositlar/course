import org.jsoup.nodes.Document

class test (file: Document) {
    val table = file.select("table") //Выделяем из всего входного файла таблицу для работы
    val rows = table.select("tr")

    fun testingRows (file: Document) {
        var row: String
        for (i in 1 until 14) {
            row = rows.get(0).text()
            println(row)
        }
    }
}