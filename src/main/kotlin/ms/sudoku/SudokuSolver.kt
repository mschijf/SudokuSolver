package ms.sudoku

import ms.ms.sudoku.SudokuStructureVijfling
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

class SudokuSolver(initialValueMap: Map<Point, Int>) {

    private val sudoku = SudokuStructureVijfling()

    init {
        sudoku.allCells
            .filter { it.pos in initialValueMap }
            .forEach {
                it.setValue(initialValueMap[it.pos]!!)
            }
    }

    fun solve() {
        println("start nog onbekend: ${sudoku.allCells.count { it.isNotSolved() }}")
        while (sudoku.allCells.any { it.isNotSolved() }) {
            val solvablePair = findNextStep()
            if (solvablePair != null) {
                solvablePair.first.setValue(solvablePair.second)
            } else {
                println("No solution found with algoritme 1 en algorithm 2")
                break
            }
        }

        println("einde alles: nog onbekend: ${sudoku.allCells.count { it.isNotSolved() }}")
        if (sudoku.allGroups.all{it.verifyOk()}) {
            println("Verified OK")
        } else {
            println("Verified NOT ok")
        }
    }

    private fun findNextStep(): Pair<Cell, Int>? {
        val solvablePair = algorithm1()
        if (solvablePair != null)
            return solvablePair
        return algorithm2()
    }

    private fun algorithm1(): Pair<Cell, Int>? {
        return sudoku.allCells
            .filter{cell->cell.isNotSolved()}
            .firstOrNull { cell -> cell.possibleValues.size == 1 }
            ?.let { Pair(it, it.possibleValues.first()) }
    }

    private fun algorithm2(): Pair<Cell, Int>? {
        return sudoku.allGroups
            .firstOrNull { it.hasUnqiueCellValue() != null }
            ?.hasUnqiueCellValue()
    }

    private fun Cell.setValue(value: Int) {
        this.value = value
        this.possibleValues.clear()
        val groups = sudoku.getCellGroups(this)
        groups.forEach { group ->
            group.cellList.forEach { cell ->
                cell.possibleValues -= value
            }
        }
    }

    fun printSudoku() {
        val cellMap = sudoku.allCells.associateBy { it.pos }
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
//
//    private fun makeCellMap(): Map<Point, Cell> {
//        return (0..20)
//            .flatMap { y -> (0..20).map { x -> Cell(pos(x, y), null, mutableSetOf(1,2,3,4,5,6,7,8,9)) } }
//            .associateBy { it.pos }
//    }
//
//
//    private fun makeHorizontalLineGroups(): List<Group> {
//        val rows1 = (0..8).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//        val rows2 = (0..8).map {y -> (12..20).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//
//        val rows3 = (6..14).map {y -> (6..14).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//
//        val rows4 = (12..20).map {y -> (0..8).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//        val rows5 = (12..20).map {y -> (12..20).map { x -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//
//        return rows1 + rows2 + rows3 + rows4 + rows5
//    }
//
//    private fun makeVerticalLineGroups(): List<Group> {
//        val cols1 = (0..8).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//        val cols2 = (0..8).map {x -> (12..20).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//
//        val cols3 = (6..14).map {x -> (6..14).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//
//        val cols4 = (12..20).map {x -> (0..8).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//        val cols5 = (12..20).map {x -> (12..20).map { y -> cellMap[pos(x,y)]!! } }.map{ cellList -> Group(cellList) }
//
//        return cols1 + cols2 + cols3 + cols4 + cols5
//    }
//
//    private fun makeSquareGroups(): List<Group> {
//        val squares1 = this.makeLocalSquareGroups(pos(0,0))
//        val squares2 = this.makeLocalSquareGroups(pos(12,0))
//
//        val squares3 = this.makeLocalSquareGroups(pos(6,6))
//
//        val squares4 = this.makeLocalSquareGroups(pos(0,12))
//        val squares5 = this.makeLocalSquareGroups(pos(12,12))
//
//        return squares1 + squares2 + squares3 + squares4 + squares5
//    }
//
//    private fun makeLocalSquareGroups(upperLeftPoint: Point): List<Group> {
//        val result = mutableListOf<Group>()
//        for (i in 0..2) {
//            for (j in 0..2) {
//                val squareCells = (0+i*3..2+i*3).flatMap { y -> (0+j*3..2+j*3)
//                    .map { x -> cellMap[pos(upperLeftPoint.x + x, upperLeftPoint.y + y)]!! }
//                }
//                result += Group(squareCells)
//            }
//        }
//        return result
//    }
}