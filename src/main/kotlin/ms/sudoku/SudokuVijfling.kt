package ms.sudoku

import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import kotlin.collections.flatMapIndexed
import kotlin.text.mapIndexed

class SudokuVijfling(filledValues: List<Pair<Point, Int>>) {
    private val cellMap = makeCellMap()
    private val allGroups: List<Group> = makeHorizontalLineGroups() + makeVerticalLineGroups() + makeSquareGroups()
    private val allCells: List<Cell> = allGroups.flatMap { it.cellList }.distinct()

    init {
        filledValues.forEach { (pos, value) ->
            allCells.first { it.pos == pos }.setValue(value)
        }
    }

    fun solve() {
        println("start nog onbekend: ${allCells.count { it.isNotSolved() }}")
        while (allCells.any { it.isNotSolved() }) {
            val solvableCell = algorithm1()
            if (solvableCell != null) {
                solvableCell.setValue(solvableCell.possibleValues.first())
            } else {
                val solvablePair = algorithm2()
                if (solvablePair != null) {
                    solvablePair.first.setValue(solvablePair.second)
                } else {
                    println("No soultion found with algoritme 1 en algorithm 2")
                    break
                }
            }
        }

        println("einde alles: nog onbekend: ${allCells.count { it.isNotSolved() }}")
        if (allGroups.all{it.verifyOk()}) {
            println("Verified OK")
        } else {
            println("Verified NOT ok")
        }
    }

    private fun algorithm1(): Cell? {
        println("algo1 candidates: ${allCells.filter{cell->cell.isNotSolved()}.count{cell -> cell.possibleValues.size == 1}}")
        return allCells
            .filter{cell->cell.isNotSolved()}
            .firstOrNull { cell -> cell.possibleValues.size == 1 }
    }

    private fun algorithm2(): Pair<Cell, Int>? {
        return allGroups.firstOrNull { it.hasUnqiueCellValue() != null }?.hasUnqiueCellValue()
    }

    private fun Cell.setValue(value: Int) {
        this.value = value
        this.possibleValues.clear()
        val groups = this.getCellGroups()
        groups.forEach { group ->
            group.cellList.forEach { cell ->
                cell.possibleValues -= value
            }
        }
    }

    private fun Cell.getCellGroups(): List<Group> {
        return allGroups.filter { it.hasCell(this) }
    }


    companion object {
        fun of(rawInput: List<String>): SudokuVijfling {
            val inputCellList = rawInput
                .flatMapIndexed { y, line ->
                    line
                        .padEnd(21, ' ')
                        .mapIndexed { x, ch ->  Pair(pos(x,y), if (ch.isDigit()) ch.digitToInt() else 0) }
                }.filter { it.second > 0 }
            return SudokuVijfling(inputCellList)
        }

    }
    
    
    fun printSudoku() {
        val cellMap = allCells.associateBy { it.pos }
        for (y in 0..20) {
            if (y % 3 == 0)
                println()
            for (x in 0..20) {
                if (x % 3 == 0)
                    if (y in (9 ..11))
                        if (x in (6..15))
                            print(" | ")
                        else
                            print("   ")
                    else
                        print(" | ")
                else
                    print("   ")

                val key = pos(x, y)
                if (key in cellMap) {
                    print(cellMap[key]!!.value?:".")
                } else {
                    print(" ")
                }
            }
            if (y in (9 ..11))
                print("   ")
            else
                print(" | ")
            println()
        }
    }

    private fun makeCellMap(): Map<Point, Cell> {
        return (0..20)
            .flatMap { y -> (0..20).map { x -> pos(x, y).toCell() } }
            .associateBy { it.pos }
    }


    private fun makeHorizontalLineGroups(): List<Group> {
        val rows1 = (0..8).map {y -> (0..8).map { x -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }
        val rows2 = (0..8).map {y -> (12..20).map { x -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }

        val rows3 = (6..14).map {y -> (6..14).map { x -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }

        val rows4 = (12..20).map {y -> (0..8).map { x -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }
        val rows5 = (12..20).map {y -> (12..20).map { x -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }

        return rows1 + rows2 + rows3 + rows4 + rows5
    }

    private fun makeVerticalLineGroups(): List<Group> {
        val cols1 = (0..8).map {x -> (0..8).map { y -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }
        val cols2 = (0..8).map {x -> (12..20).map { y -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }

        val cols3 = (6..14).map {x -> (6..14).map { y -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }

        val cols4 = (12..20).map {x -> (0..8).map { y -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }
        val cols5 = (12..20).map {x -> (12..20).map { y -> pos(x,y).getCell() } }.map{ cellList -> Group(cellList) }

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
                    .map { x -> pos(upperLeftPoint.x + x, upperLeftPoint.y + y).getCell() }
                }
                result += Group(squareCells)
            }
        }
        return result
    }

    private fun Point.getCell() = cellMap[this]!!
    private fun Point.toCell() = Cell(this, null, mutableSetOf(1,2,3,4,5,6,7,8,9))

}