package ms.sudoku

import ms.ms.sudoku.SudokuStructure
import ms.ms.sudoku.SudokuType
import tool.coordinate.twodimensional.Point

class SudokuSolver(sudokuType: SudokuType, initialValueMap: Map<Point, Int>) {

    private val sudoku = SudokuStructure(sudokuType)

    init {
        sudoku.initialFill(initialValueMap)
    }

    fun solve() {
        println("start nog onbekend: ${sudoku.allCells.count { it.isNotSolved() }}")
        while (sudoku.allCells.any { it.isNotSolved() }) {
            val solutionStep = findNextStep()
            if (solutionStep != null) {
                sudoku.setValue(solutionStep.cell, solutionStep.value)
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

    fun solveRecursive(): Boolean {
        if (sudoku.allFilledIn()) {
            return true
        }
        if (sudoku.illegal() ) {
            return false
        }

        val solutionStep = findNextStep()
        var solved = false
        if (solutionStep != null) {
            fillInSolutionStep(solutionStep)
            solved = solveRecursive()
            if (!solved)
                takebackLastSolutionStep(solutionStep)
        } else {
            val mostPromisingCell = findMostPromisingCell()
            val possibleValueList = mostPromisingCell.possibleValues.toList()
            possibleValueList.map{ tryValue -> SolutionStep(mostPromisingCell, tryValue) }.forEach { solutionStep ->
                fillInSolutionStep(solutionStep)
                solved = solveRecursive()
                if (!solved)
                    takebackLastSolutionStep(solutionStep)
            }
        }
        return solved
    }

    fun countAll(): Int {
        if (sudoku.allFilledIn()) {
            return 1
        }
        if (sudoku.illegal() ) {
            return 0
        }

        val solutionStep = findNextStep()
        var count = 0
        if (solutionStep != null) {
            fillInSolutionStep(solutionStep)
            count = countAll()
            takebackLastSolutionStep(solutionStep)
        } else {
            val mostPromisingCell = findMostPromisingCell()
            val possibleValueList = mostPromisingCell.possibleValues.toList()
            possibleValueList.map{ tryValue -> SolutionStep(mostPromisingCell, tryValue) }.forEach { solutionStep ->
                fillInSolutionStep(solutionStep)
                count += countAll()
                takebackLastSolutionStep(solutionStep)
            }
        }
        return count
    }

    private fun findMostPromisingCell(): Cell {
        return sudoku.allCells.filter { it.isNotSolved() }.minBy { it.possibleValues.size }
    }

    private fun takebackLastSolutionStep(step: SolutionStep) {
        step.cell.value = null
        sudoku.recalculate()
    }

    private fun fillInSolutionStep(step: SolutionStep) {
        sudoku.setValue(cell = step.cell, value = step.value)
    }

    private fun findNextStep(): SolutionStep? {
        val solutionStep = algorithm1()
        if (solutionStep != null)
            return solutionStep
        return algorithm2()
    }

    private fun algorithm1(): SolutionStep? {
        return sudoku.allCells
            .filter{cell->cell.isNotSolved()}
            .firstOrNull { cell -> cell.possibleValues.size == 1 }
            ?.let { SolutionStep(it, it.possibleValues.first()) }
    }

    private fun algorithm2(): SolutionStep? {
        val result = sudoku.allGroups
            .firstOrNull { it.getCellWithUniqueValue() != null }
            ?.getCellWithUniqueValue()
        return if (result != null) SolutionStep(result.first, result.second) else null
    }

    fun printSudoku() {
        sudoku.printSudoku()
    }
}

data class SolutionStep(val cell: Cell, val value: Int)