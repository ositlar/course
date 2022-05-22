import kotlinx.serialization.*

@Serializable
data class TimeTable(
    val upperWeek: MutableList<Day>,
    val lowerWeek: MutableList<Day>
) {
    override fun toString() = "Верхняя неделя:\n$upperWeek\nНижняя неделя:\n$lowerWeek\n"
}
