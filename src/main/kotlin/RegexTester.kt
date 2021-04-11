fun main() {
    do{
        print("Enter text to test regex on: ")
        val text = readLine()!!
        print("Enter regex: ")
        val regex = readLine()!!
        val matches = regex.toRegex().findAll(text).toList()
        if (matches.isNotEmpty()) {
            for (match in matches) println("Match value: ${match.value} at indices: ${match.range}")
        } else {
            println("No match found.")
        }
        val another = getValidatedInput(
            "Would you like to test another regex? y or n",
            "That is not a valid input. Please enter either y or n",
            "[yn]".toRegex()
        )!!
    } while(another!="n")
}