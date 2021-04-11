import java.io.File

fun main() {
    val file = File("C:\\Users\\shmue\\OneDrive\\Desktop\\Sefaria Texts (Excluding Talmud and Tanaitic texts)")
    val walk = file.walk().filter{it.isDirectory}.toList()
    val size = walk.size
    println("$size folders found")
}
class CountFolders {
}