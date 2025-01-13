package ms.sudoku

import tool.coordinate.twodimensional.Point
import kotlin.collections.minusAssign

class Cell(
    val pos: Point,
    val possibleValues : MutableSet<Int>
) {

    var value: Int? = null
        private set

    private lateinit var inGroups: List<Group>
    fun initializeGroups(groups: List<Group>) {
        inGroups = groups
    }

    fun isSolved() = value != null
    fun isNotSolved() = !isSolved()

    fun setValue(value: Int) {
        this.value = value
        recalculate()
    }

    fun recalculate() {
        if (value != null) {
            possibleValues.clear()
            inGroups.forEach { group ->
                group.cellList.forEach { cell ->
                    cell.possibleValues -= value!!
                }
            }
        }
    }

    fun resetValue() {
        this.value = null
        this.possibleValues.clear()
    }
}
