import com.mongodb.client.MongoDatabase
import org.json.JSONObject
import org.litote.kmongo.KMongo
import org.litote.kmongo.json

val client = KMongo
    .createClient("mongodb://root:u9SF3oId7Mg6@192.168.0.12:27017") /////////////////////////////
val mongoDatabase: MongoDatabase = client.getDatabase("test")

fun prettyPrintJson(json: String) =
    println(JSONObject(json)
        .toString(4))

fun prettyPrintCursor(cursor: Iterable<*>) =
    prettyPrintJson("{ Результат: ${cursor.json} }")