fun main() {
    val shape = getValidatedInput("What shape would you like to calculate? Please enter either \"square\" or \"rectangle\"",
        "Invalid input. Please enter either \"square\" or \"rectangle\"",
    "(square)|(rectangle)".toRegex())
    val lengthOfSideA = getValidatedInput("Please enter length of side ${if(shape=="square") "" else "a"}","Please enter a whole number","\\d+".toRegex())
    val lengthOfSideB = if(shape == "square") lengthOfSideA else getValidatedInput("Please enter length of side b","Please enter a whole number","\\d+".toRegex())
    val units = "in."
//   val areaOfRectangle = getAreaOfRectangle(lengthOfSideA,lengthOfSideB)
//    println(Math.pow(lengthOfSideA!!.toDouble(), lengthOfSideB!!.toDouble()))
//    println("The max value of a Double is ${Double.MAX_VALUE}")
//    println("The area of your rectangle is $areaOfRectangle $units.")
}
fun getAreaOfSquare(lengthOfSide:Int):Int{
    return getAreaOfRectangle(lengthOfSide, lengthOfSide)
}
fun getAreaOfRectangle(lengthOfSideA: Int, lengthOfSideB: Int):Int{
    return lengthOfSideA*lengthOfSideB
}
fun convertInchesToFeet(inches:Int): Double {
    return inches/(12.toDouble())
}

