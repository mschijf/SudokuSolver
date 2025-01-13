package ms.sudoku

import tool.coordinate.twodimensional.Point

class Cell(
    val pos: Point,
    var value: Int? = null) {

    private lateinit var inGroups: List<Group>
    fun initializeGroups(groups: List<Group>) {
        inGroups = groups
    }

    fun inGroups() = inGroups

    fun isSolved() = value != null
    fun isNotSolved() = !isSolved()

    override fun toString(): String = "$pos: $value"
}
