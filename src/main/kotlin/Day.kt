import kotlinx.serialization.*

@Serializable
class Day (
    var dayOfWeek: String,
    var classes: MutableList<Lesson?>
        ) {
    override fun toString() = "\n$dayOfWeek\n$classes"
}