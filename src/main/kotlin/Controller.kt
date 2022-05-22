import org.apache.poi.xssf.usermodel.*

class Controller (timeTable: XSSFWorkbook) {
    val tableGroup = timeTable.getSheetAt(0) //Берем из файла лист, в котором содержится расписание
    fun collecting (table: XSSFSheet): MutableList<Group> {
        val groups: MutableList<Group> = mutableListOf() //Коллекция для всех крупп
        var currentGroup: Group //Группа, с расписанием которой программа работает
        var i = 0 //Итератор для списка групп
        while (table.getRow(16 * i + 0)?.getCell(0) != null) { //Цикл не завершается, пока строка не пустая
            currentGroup = Group(groupName = table.getRow(16 * i + 0).getCell(0).toString().substringAfter(": "), collectingForOneGroup(i,table))//Определяем группу
            groups.add(currentGroup) //Добавление группы в общую коллекцию
            i++
        }
        return groups//возвращает колекцию с расписанием всех групп
    }

    fun collectingForOneGroup (idGroup: Int, table: XSSFSheet): TimeTable {
        val timeTable = TimeTable(mutableListOf(), mutableListOf()) //Заполняем объект пустыми списками, далее их заполняем
        var lessonToday: Lesson?
        var row = 16 * idGroup + 4 //начальная строка
        var cell = 1 //начальный столбец
        val lastRow = row + 12 //следующая строка
        val lastCell = 5 //конечный столбец
        var upperWeekDay: Day
        var lowerWeekDay: Day
        while (row < lastRow) {
            upperWeekDay = Day(table.getRow(row).getCell(0).toString(), mutableListOf()) //Передаем в объекты название дня недели и пары в этот день (26 и 27 строки)
            lowerWeekDay = Day(table.getRow(row).getCell(0).toString(), mutableListOf())
            while (cell <= lastCell) {//Перемещаемся по ячейка с 1 до 5 и заносим предметы в объект lessonToday
                if (table.getRow(row).getCell(cell).toString().filter { !it.isWhitespace() } != "") {
                    val one = table.getRow(row).getCell(cell).toString().split("ст.пр.")
                    val two = table.getRow(row).getCell(cell).toString().split("доц.")
                    val three = table.getRow(row).getCell(cell).toString().split(" проф.")
                    val PE = table.getRow(row).getCell(cell).toString().contains("Физкультура",ignoreCase = true) //Для физкультуры
                        if (!PE) {
                            if ((one.size == 1) && (three.size == 1)) { //Доцент
                                lessonToday = Lesson(
                                    table.getRow(row).getCell(cell).toString().substringBefore("."),//тип
                                    table.getRow(row).getCell(cell).toString().substringAfter("доц.").substringBefore("а."),//преподаватель
                                    table.getRow(row).getCell(cell).toString().substringBefore("доц.").substringAfter(".")
                                        .substringBefore("- 1").substringBefore("-2"), //предмет
                                    table.getRow(row).getCell(cell).toString().substringAfter("а.")//аудитория
                                        )
                            } else {
                                if ((one.size == 1) && (two.size == 1)) //Профессор
                                {
                                    lessonToday = Lesson(
                                        table.getRow(row).getCell(cell).toString().substringBefore("."),// тип
                                        table.getRow(row).getCell(cell).toString().substringAfter("проф.").substringBefore("а."),//преподаватель
                                        table.getRow(row).getCell(cell).toString().substringBefore("проф.").substringAfter(".")
                                            .substringBefore("- 1").substringBefore("-2"), //предмет
                                        table.getRow(row).getCell(cell).toString().substringAfter("а.")//аудитория
                                    )
                                } else { //Старший преподаватель
                                    lessonToday = Lesson(
                                        table.getRow(row).getCell(cell).toString().substringBefore("."),// тип
                                        table.getRow(row).getCell(cell).toString().substringAfter("ст.пр.").substringBefore("а."),//преподаватель
                                        table.getRow(row).getCell(cell).toString().substringBefore("ст.пр.").substringAfter(".")
                                            .substringBefore("- 1").substringBefore("-2"), //предмет
                                        table.getRow(row).getCell(cell).toString().substringAfter("а.")//аудитория
                                                )
                                }
                            }
                        } else {//исключение для пары "Физкультура"
                            lessonToday = Lesson(
                                "пр",//тип
                                "-",//преподававтель
                                table.getRow(row).getCell(cell).toString().substringBefore("а."),//предмет
                                table.getRow(row).getCell(cell).toString().substringAfter("а.")//аудитория
                            )
                        }
                }
                else
                {
                    lessonToday = null
                }
                if(row % 16 <= 9)
                    upperWeekDay.classes.add(lessonToday)
                else
                    lowerWeekDay.classes.add(lessonToday)
                cell++
            }
            timeTable.upperWeek.add(upperWeekDay)
            timeTable.lowerWeek.add(lowerWeekDay)
            row++
            cell = 1
        }
        return timeTable//Возвращает расписание для конкертной группы
    }
    val groups = collecting(tableGroup)
}