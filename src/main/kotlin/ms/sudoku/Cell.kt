package ms.sudoku

import tool.coordinate.twodimensional.Point

data class Cell(
    val pos: Point,
    var value: Int?,
    val possibleValues : MutableSet<Int>
) {

    fun isSolved() = value != null
    fun isNotSolved() = !isSolved()

}
