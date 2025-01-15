package ms.sudoku

import tool.coordinate.twodimensional.Point

abstract class SudokuStructure() {

    val defaultValueSet = setOf(1,2,3,4,5,6,7,8,9)

    val allGroups: List<Group> = makeGroups()
    val allCells: List<Cell> = allGroups.flatMap { it.cellList }.distinct()

    init {
        allCells.forEach { cell ->
            cell.inGroups = allGroups.filter { it.hasCell(cell) }
        }
    }

    protected abstract fun makeGroups(): List<Group>

    fun initialFill(initialValueMap: Map<Point, Int>) {
        allCells
            .filter { it.pos in initialValueMap }
            .forEach { cell -> cell.value = initialValueMap[cell.pos]!! }
    }

    fun allFilledIn(): Boolean {
        return allCells.all { cell -> cell.isSolved() }
    }

    abstract fun printSudoku()

}