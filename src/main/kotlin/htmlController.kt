import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class htmlController (file: Document) {
    val table = file.select("table") //Выделяем из всего входного файла таблицу для работы
    val rows = table.select("tr")
    val groups = collecting(file)
    fun collecting (file: Document): MutableList<Group> {
        val groups: MutableList<Group> = mutableListOf() //Коллекция для всех групп
        //var i = 0 //Итератор для списка групп / idGroup
        var currentGroup: Group =
            Group(groupName = "20з", tableAnalysis(file)) //Группа, с расписанием которой программа работает
        groups.add(currentGroup)
        return groups
    }
    fun tableAnalysis (file: Document): TimeTable {
        val timeTable = TimeTable(mutableListOf(), mutableListOf()) //Создания объекта расписания
        var lessonToday: Lesson? //Создание объекта пары, которую обрабатываем в данный момент
        val lastRow = rows.size - 1 //Определяем последнюю строку, затем на основе этого значения высчитываем принадлежность дня недели к верхней или нижней неделе
        var upperWeekDay: Day //Создание объектов дня недели для каждого типа недели
        var lowerWeekDay: Day
        val lastCell = 6 //Максимум 5 пар в день, поэтому работаем со столбцами 0-5, где 1-5 - пары, 0 - название дня недели
        var i = 1 //Итератор столбцов
        for (rowIterator in 2 until 14) {       //Индексы строк начинаютсяя с 0, таблица заканчивается на 14 строке, то есть на 13-ом индексе
            upperWeekDay = Day(rows[rowIterator].select("td")[0].text(), mutableListOf()) //Заносим в объекты название дня недели
            lowerWeekDay = Day(rows[rowIterator].select("td")[0].text(), mutableListOf())
            var i = 1 //Итератор столбцов
            while (i < lastCell) {
                val coll = rows[rowIterator].select("td")
                if (coll[i].text().filter { !it.isWhitespace() } != "") {
                    val one = coll[i].text().split("ст.пр")
                    val two = coll[i].text().split("доц.")
                    val three = coll[i].text().split("проф.")
                    val PE = coll[i].text().contains("Физкультура", ignoreCase = true)
                    if (!PE) {
                        if ((one.size == 1) && (three.size == 1)) {
                            lessonToday = Lesson(
                                coll[i].text().substringBefore("."),
                                coll[i].text().substringAfter("доц.").substringBefore("а."),
                                coll[i].text().substringBefore("доц.").substringAfter(".")
                                    .substringBefore("- 1").substringBefore("-2"),
                                coll[i].text().substringAfter("а.")
                            )
                        } else if ((one.size == 1) && (two.size == 1)) {
                            lessonToday = Lesson(
                                coll[i].text().substringBefore("."),
                                coll[i].text().substringAfter("ст.пр.").substringBefore("а."),
                                coll[i].text().substringBefore("проф.").substringAfter(".")
                                    .substringBefore("- 1").substringBefore("-2"),
                                coll[i].text().substringAfter("а.")
                            )
                        } else {
                            lessonToday = Lesson(
                                coll[i].text().substringBefore("."),
                                coll[i].text().substringAfter("ст.пр.").substringBefore("а."),
                                coll[i].text().substringBefore("ст.пр.").substringAfter(".")
                                    .substringBefore("- 1").substringBefore("-2"),
                                coll[i].text().substringAfter("а.")
                            )
                        }
                    } else {
                        lessonToday = Lesson(
                            "пр.",
                            "-",
                            coll[i].text().substringBefore("a."),
                            coll[i].text().substringAfter("a.")
                        )
                    }
                } else {
                    lessonToday = null
                }
                if ((lastRow - rowIterator) >= 6 ) { //Определяем тип недели
                    upperWeekDay.classes.add(lessonToday)
                } else {
                    lowerWeekDay.classes.add(lessonToday)
                }
                timeTable.upperWeek.add(upperWeekDay) //Заносим обработанную неделю в расписание
                timeTable.lowerWeek.add(lowerWeekDay)
                i++
            }
        }
        return timeTable
    }
}
