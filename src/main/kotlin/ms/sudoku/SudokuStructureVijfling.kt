package ms.sudoku

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import tool.coordinate.twodimensional.printAsGrid

class SudokuStructureVijfling() : SudokuStructure () {

    override fun makeGroups(): List<Group> {
        val cellMap = makeCellMap()
        return makeHorizontalLineGroups(cellMap) + makeVerticalLineGroups(cellMap) + makeSquareGroups(cellMap)
    }

    private fun makeCellMap(): Map<Point, Cell> {
        return (0..20)
            .flatMap { y -> (0..20).map { x -> Cell(pos(x, y) ) } }
            .associateBy { it.pos }
    }

    private fun makeHorizontalLineGroups(cellMap: Map<Point, Cell>): List<Group> {
        val rows1 = (0..8).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val rows2 = (0..8).map {y -> (12..20).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val rows3 = (6..14).map {y -> (6..14).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val rows4 = (12..20).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val rows5 = (12..20).map {y -> (12..20).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        return rows1 + rows2 + rows3 + rows4 + rows5
    }

    private fun makeVerticalLineGroups(cellMap: Map<Point, Cell>): List<Group> {
        val cols1 = (0..8).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val cols2 = (0..8).map {x -> (12..20).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val cols3 = (6..14).map {x -> (6..14).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        val cols4 = (12..20).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
        val cols5 = (12..20).map {x -> (12..20).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }

        return cols1 + cols2 + cols3 + cols4 + cols5
    }

    private fun makeSquareGroups(cellMap: Map<Point, Cell>): List<Group> {
        val squares1 = this.makeLocalSquareGroups(pos(0,0), cellMap)
        val squares2 = this.makeLocalSquareGroups(pos(12,0), cellMap)

        val squares3 = this.makeLocalSquareGroups(pos(6,6), cellMap)

        val squares4 = this.makeLocalSquareGroups(pos(0,12), cellMap)
        val squares5 = this.makeLocalSquareGroups(pos(12,12), cellMap)

        return squares1 + squares2 + squares3 + squares4 + squares5
    }

    private fun makeLocalSquareGroups(upperLeftPoint: Point, cellMap: Map<Point, Cell>): List<Group> {
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


    private fun squareStart(pos: Point): Boolean {
        if (pos.x % 3 != 0)
            return false
        if (pos.y !in (9..11))
            return true
        return pos.x in (6..15)
    }

    private fun squareEnd(pos: Point) : Boolean {
        return (pos.x == 20) && (pos.y !in (9..11))
    }

    private fun printTopLine(pos: Point) {
        if (pos.x != 0)
            return
        if (pos.y in (6..15 step 3))
            println(" +-----------+-----------+-----------+-----------+-----------+-----------+-----------+")
        else if (pos.y % 3 == 0)
            println(" +-----------+-----------+-----------+           +-----------+-----------+-----------+")
    }

    override fun printSudoku() {
        val cellMap = allCells.associateBy { it.pos }
        allCells.map { it.pos }.printAsGrid { pos ->
            printTopLine(pos)
            if (squareStart(pos)) print(" | ") else print("   ")
            val cellChar = if (pos in cellMap) cellMap[pos]!!.value?.toString() ?: "." else " "
            if (squareEnd(pos)) "$cellChar |" else cellChar
        }
        printTopLine(pos(0,21))
    }
}
