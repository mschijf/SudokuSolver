package ms.sudoku

import tool.coordinate.twodimensional.Point

data class Cell(
    val pos: Point
) {
    var value: Int? = null

    var inGroups: List<Group> = emptyList()
        set(groups) { if (field.isEmpty()) field = groups else throw Exception("inGroups was already assigned") }
        get() = field

    fun isSolved() = (value != null)
    fun isNotSolved() = !isSolved()

    override fun toString(): String = "$pos: $value"
}
