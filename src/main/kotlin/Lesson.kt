import kotlinx.serialization.*

@Serializable
data class Lesson(
    val type: String?,
    val teacher: String?,
    val name: String?,
    val classroom: String?
) {
    override fun toString() = when (type) {
        null -> "\n: $classroom, "
        else -> "\n Classroom: $classroom, Type:$type,Name:$name,Teacher:$teacher"
    }
}