package ms.ms.sudoku

import ms.sudoku.Cell
import ms.sudoku.Group
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

class SudokuStructureVijfling {

    private val cellMap = makeCellMap()
    val allGroups: List<Group> = makeHorizontalLineGroups() + makeVerticalLineGroups() + makeSquareGroups()
    val allCells: List<Cell> = allGroups.flatMap { it.cellList }.distinct()

    fun getCellGroups(aCell: Cell): List<Group> {
        return allGroups.filter { it.hasCell(aCell) }
    }

    private fun makeCellMap(): Map<Point, Cell> {
        return (0..20)
            .flatMap { y -> (0..20).map { x -> Cell(pos(x, y), null, mutableSetOf(1,2,3,4,5,6,7,8,9)) } }
            .associateBy { it.pos }
    }

    private fun makeHorizontalLineGroups(): List<Group> {
        val rows1 = (0..8).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val rows2 = (0..8).map {y -> (12..20).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val rows3 = (6..14).map {y -> (6..14).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val rows4 = (12..20).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val rows5 = (12..20).map {y -> (12..20).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        return rows1 + rows2 + rows3 + rows4 + rows5
    }

    private fun makeVerticalLineGroups(): List<Group> {
        val cols1 = (0..8).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val cols2 = (0..8).map {x -> (12..20).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val cols3 = (6..14).map {x -> (6..14).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val cols4 = (12..20).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val cols5 = (12..20).map {x -> (12..20).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        return cols1 + cols2 + cols3 + cols4 + cols5
    }

    private fun makeSquareGroups(): List<Group> {
        val squares1 = this.makeLocalSquareGroups(pos(0,0))
        val squares2 = this.makeLocalSquareGroups(pos(12,0))

        val squares3 = this.makeLocalSquareGroups(pos(6,6))

        val squares4 = this.makeLocalSquareGroups(pos(0,12))
        val squares5 = this.makeLocalSquareGroups(pos(12,12))

        return squares1 + squares2 + squares3 + squares4 + squares5
    }

    private fun makeLocalSquareGroups(upperLeftPoint: Point): List<Group> {
        val result = mutableListOf<Group>()
        for (i in 0..2) {
            for (j in 0..2) {
                val squareCells = (0+i*3..2+i*3).flatMap { y -> (0+j*3..2+j*3)
                    .map { x -> cellMap[pos(upperLeftPoint.x + x, upperLeftPoint.y + y)]!! }
                }
                result += Group(squareCells)
            }
        }
        return result
    }
}