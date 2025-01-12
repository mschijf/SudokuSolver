package ms.sudoku

import ms.ms.sudoku.SudokuStructure
import ms.ms.sudoku.SudokuType
import tool.coordinate.twodimensional.Point

class SudokuSolver(sudokuType: SudokuType, initialValueMap: Map<Point, Int>) {

    private val sudoku = SudokuStructure(sudokuType)

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
        sudoku.printSudoku()
    }
}