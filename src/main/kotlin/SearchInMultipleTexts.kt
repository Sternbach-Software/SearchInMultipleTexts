import java.awt.FileDialog
import java.io.File
import java.util.*
import kotlin.system.measureNanoTime

fun main() {
    var newFile = "n"
    do {
        print("Input pathname to parent folder of texts to search: ")
        val pathname =
            readLine()!! //?: "C:\\Users\\shmue\\OneDrive\\Desktop\\Sefaria Texts (Excluding Talmud and Tanaitic texts)\\Tanach"
        val filenameOrExtensionString = getValidatedInput(
            "Would you like to search files with a certain filename" +
                    "(e.g. only files with the name \"Tanach with Text Only.txt\"), or with a certain extension" +
                    "(e.g. only files with the \"txt\" extension)? " +
                    "Enter 1 for filename or 2 for extension",
            "That is not a valid input. Please enter either 1 or 2",
            "[12]".toRegex()
        )!!
        val filenameOrExtensionInt = filenameOrExtensionString.toInt()
        val searchCondition: (File) -> Boolean
        when (filenameOrExtensionInt) {
            1 -> {
                val filename = getValidatedInput(
                    "Enter filename you would like to search in",
                    "Please enter a valid filename",
                    "^(?!^(PRN|AUX|CLOCK$|NUL|CON|COM\\d|LPT\\d|\\..*)(\\..+)?$)[^\\x00-\\x1f\\\\<>:\"/|?*;]+$".toRegex()
                )
                searchCondition = { file: File ->
                    file.name == filename /*&& file.absolutePath.contains("English")*/
                }
            }
            else -> {
                val extension = getValidatedInput(
                    "Enter extension you would like to search in",
                    "Please enter an extension with only letters or numbers (e.g. txt, mp3, etc.)",
                    "[\\w]+".toRegex()
                )!!
                searchCondition = { file: File ->
                    file.extension == extension
                }
            }
        }


        val folder = File(pathname)
        val filesInFolder: List<File>
        val fileTreeWalk: FileTreeWalk
        val timeToWalk = measureNanoTime {
            fileTreeWalk = folder.walk()
        }
        val filteredWalk: Sequence<File>
        val timeToFilter = measureNanoTime { filteredWalk = fileTreeWalk.filter(searchCondition) }
        val timeToList = measureNanoTime { filesInFolder = filteredWalk.toList() }
        println("Time to list = ${timeToList / 1000000000}")
        println("Time to filter = ${timeToFilter / 1000000000}")
        println("Time to walk = ${timeToWalk / 1000000000}")
        val numberOfFiles = filesInFolder.size
        println("Number of files = $numberOfFiles")
        val mutableListOfStrings = mutableListOf<String>()
        addTextsToList(numberOfFiles, filesInFolder, mutableListOfStrings)
        do {
            var found = false
            print("Enter phrase (or regex) you would like to search for: ")
            var numberOfOccurences = 0
            var numberOfFilesMatches = 0
            val searchPhrase = readLine()!!.toRegex()
            for (text in mutableListOfStrings) {
                if (text.contains(searchPhrase)) {
                    found = true
                    numberOfFilesMatches++
                    var absolutePathToSefer = filesInFolder[mutableListOfStrings.indexOf(text)].absolutePath
                    if (absolutePathToSefer.contains("C:\\Users\\shmue\\OneDrive\\Desktop\\Sefaria Texts (Excluding Talmud and Tanaitic texts)\\Tanach\\")) absolutePathToSefer =
                        absolutePathToSefer.removePrefix(
                            "C:\\Users\\shmue\\OneDrive\\Desktop\\Sefaria Texts (Excluding Talmud and Tanaitic texts)\\Tanach\\"
                        )
                    val matches = searchPhrase.findAll(text).toList()
                    val numberOfMatches = matches.size
                    numberOfOccurences += numberOfMatches
                    println("Search phrase found${if (numberOfMatches == 1) " 1 time " else if (numberOfMatches <= 0) " " else " $numberOfMatches times "}in file: $absolutePathToSefer")

//            val indexOfFind = searchPhrase.findAll(text).toList().size
                    for (match in matches) {
                        val indexOfFind = match.range.first
                        val i = 30
                        val endIndex = if ((indexOfFind + i) > text.length) text.length else indexOfFind + i
                        val startIndex = if ((indexOfFind - i) < 0) 0 else indexOfFind - i
                        println("indexOfFind=$indexOfFind,text.length=${text.length},startIndex=$startIndex,endIndex=$endIndex")
                        println(
                            "The text found (at index $indexOfFind in file) is: ${
                                text.substring(
                                    startIndex,
                                    endIndex
                                )
                            }"
                        )
                    }
                }
            }
            if (!found) println("The search phrase \"$searchPhrase\" was not found.")
            else println("Total number of occurrences is $numberOfOccurences in $numberOfFilesMatches different files")
            val another = getValidatedInput(
                "Would you like to find another regex? y or n",
                "That is not a valid input. Please enter either y or n",
                "[yn]".toRegex()
            )!!
            if(another == "y") newFile = getValidatedInput("Would you like to search in a different folder? y or n",
                "That is not a valid input. Please enter either y or n",
                "[yn]".toRegex()
            )!!
        } while(another == "y" && newFile == "n")
    } while(newFile == "y")
}

private fun addTextsToList(
    numberOfFiles: Int,
    filesInFolder: List<File>,
    mutableListOfStrings: MutableList<String>
) {
    val percentageUnit = numberOfFiles / 100.0
    for (index in 1..numberOfFiles) {
        val file = filesInFolder[index - 1]
            val bufferedReader = file.bufferedReader(bufferSize = 60_000)
        var text = ""
        try {
            text = bufferedReader.readText()
        }
        catch (e: OutOfMemoryError){
            val absolutePath = file.absolutePath
            println("File too big: $absolutePath. Trying again.")
            try{
                val stringBuilder = getLargeFile(absolutePath)
                text = stringBuilder.toString()
                println("Success.")
            }catch (e: Throwable){
                println("File too big again. Skipping.")
            }

        }
        mutableListOfStrings.add(text)
        bufferedReader.close()
//        if (index % percentageUnit == 0)
//            println("Percentage done: ${(index / percentageUnit)}%")
    }
}

fun getValidatedInput(firstMessageToDisplay: String, messageToDisplayOnError: String, regex: Regex): String? {
    print("$firstMessageToDisplay: ")
    var input = readLine()
    /*!(input.matches(regex))*/
    while(input?.matches(regex)?.not() == true) /*doesn't match regex (written in a strange way to keep nullability)*/{
        print("$messageToDisplayOnError: ")
        input = readLine()
    }
    return input
}

fun getLargeFile(pathname: String): StringBuilder {
val list = mutableListOf<String>()
    Scanner(File(pathname), "UTF-8").use { sc ->
        while (sc.hasNextLine()) {
            list.add(sc.nextLine())
        }
        if (sc.ioException() != null) {
            println("File too big: $pathname")
        }
    }
    val stringBuilder = StringBuilder()
    list.forEach{stringBuilder.append(it)}
    return stringBuilder
}



class Main {
}