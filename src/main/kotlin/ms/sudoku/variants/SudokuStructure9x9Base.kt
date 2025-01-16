package ms.sudoku.variants

import ms.sudoku.base.Cell
import ms.sudoku.base.Group
import ms.sudoku.base.SudokuStructure
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import kotlin.collections.plus
import kotlin.collections.plusAssign

abstract class SudokuStructure9x9Base() : SudokuStructure() {

    override fun getDefaultSetDefinition() = setOf(1,2,3,4,5,6,7,8,9)

    override fun makeGroups(): List<Group> {
        val cellMap = makeCellMap()
        return makeHorizontalLineGroups(cellMap) +
                makeVerticalLineGroups(cellMap) +
                makeSquareGroups(cellMap) +
                makeExtraGroups(cellMap)
    }

    protected abstract fun makeExtraGroups(cellMap: Map<Point, Cell>): List<Group>

    private fun makeCellMap(): Map<Point, Cell> {
        return (0..8)
            .flatMap { y -> (0..8).map { x -> Cell(pos(x, y)) } }
            .associateBy { it.pos }
    }

    private fun makeHorizontalLineGroups(cellMap: Map<Point, Cell>): List<Group> {
        return (0..8).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
    }

    private fun makeVerticalLineGroups(cellMap: Map<Point, Cell>): List<Group> {
        return (0..8).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
    }

    private fun makeSquareGroups(cellMap: Map<Point, Cell>): List<Group> {
        val result = mutableListOf<Group>()
        for (i in 0..2) {
            for (j in 0..2) {
                val squareCells = (0+i*3..2+i*3).flatMap { y -> (0+j*3..2+j*3)
                    .map { x -> cellMap[pos(x, y)]!! }
                }
                result += Group(squareCells)
            }
        }
        return result
    }

    override fun printSudoku() {
        val cellMap = allCells.associateBy { it.pos }
        for (y in 0..8) {
            if (y % 3 == 0)
                println(" +-----------+-----------+-----------+")
            for (x in 0..8) {
                print(if (x % 3 == 0) " | " else "   ")
                print(cellMap[pos(x, y)]?.value ?: ".")
            }
            println(" |")
        }
        println(" +-----------+-----------+-----------+")
    }
}


