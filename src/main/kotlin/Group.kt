import kotlinx.serialization.*

@Serializable
data class Group(
    val groupName: String,
    val table: TimeTable
) {
    override fun toString() = "\n$groupName\n$table\n"
}
