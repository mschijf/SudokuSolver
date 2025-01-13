package ms.sudoku

import ms.ms.sudoku.SudokuStructure

class SudokuSolver(val sudoku: SudokuStructure) {

    private val possibleCellValues = sudoku.allCells.associate { it to mutableSetOf<Int>() }

    init {
        recalculatePossibleValues()
    }

    fun solveRecursive(): Boolean {
        if (sudoku.allFilledIn()) {
            return true
        }
        if (illegal() ) {
            return false
        }

        val solutionStep = findNextStep()
        var solved = false
        if (solutionStep != null) {
            fillInSolutionStep(solutionStep)
            solved = solveRecursive()
            if (solved)
                return true
            takeBackLastSolutionStep(solutionStep)
        } else {
            val mostPromisingCell = findMostPromisingCell()
            val possibleValueList = mostPromisingCell.possibleValues().toList()
            possibleValueList.map{ tryValue -> SolutionStep(mostPromisingCell, tryValue) }.forEach { solutionStep ->
                fillInSolutionStep(solutionStep)
                solved = solveRecursive()
                if (solved)
                    return true
                takeBackLastSolutionStep(solutionStep)
            }
        }
        return false
    }

    val sudokuHashValue = Array<Int>(81) { 0 }
    val cache = mutableMapOf<Array<Int>, Long>()

//    var totalCount = 0
    fun countAll(): Long {
        if (sudoku.allFilledIn()) {
//            totalCount++
//            if (totalCount % 10_000 == 0) {
//                println("counted: $totalCount, upperleft ${sudoku.allCells[0]}, middle: ${sudoku.allCells[40]}")
//            }
            return 1L
        }
        if (illegal() ) {
            return 0L
        }
        if (sudokuHashValue in cache) {
            return cache[sudokuHashValue]!!
        }

        val solutionStep = findNextStep()
        var count = 0L
        if (solutionStep != null) {
            fillInSolutionStep(solutionStep)
            count = countAll()
            takeBackLastSolutionStep(solutionStep)
        } else {
            val mostPromisingCell = findMostPromisingCell()
            val possibleValueList = mostPromisingCell.possibleValues().toList()
            possibleValueList.map{ tryValue -> SolutionStep(mostPromisingCell, tryValue) }.forEach { solutionStep ->
                fillInSolutionStep(solutionStep)
                count += countAll()
                takeBackLastSolutionStep(solutionStep)
            }
        }
        cache[sudokuHashValue] = count
        return count
    }

    private fun findMostPromisingCell(): Cell {
        return sudoku.allCells.filter { it.isNotSolved() }.minBy { cell -> cell.possibleValues().size }
    }

    private fun fillInSolutionStep(step: SolutionStep) {
        sudokuHashValue[step.cell.pos.x*9 + step.cell.pos.y] = step.value
        step.cell.setValue(step.value)
    }

    private fun takeBackLastSolutionStep(step: SolutionStep) {
        sudokuHashValue[step.cell.pos.x*9 + step.cell.pos.y] = 0
        step.cell.resetValue()
        recalculatePossibleValues()
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
            .firstOrNull { cell -> cell.possibleValues().size == 1 }
            ?.let { cell -> SolutionStep(cell, cell.possibleValues().first()) }
    }

    private fun algorithm2(): SolutionStep? {
        val result = sudoku.allGroups
            .firstOrNull { it.getCellWithUniqueValue() != null }
            ?.getCellWithUniqueValue()
        return if (result != null) SolutionStep(result.first, result.second) else null
    }


    fun illegal(): Boolean {
        return sudoku.allCells.any { cell -> cell.isNotSolved() && cell.possibleValues().isEmpty() }
    }

    fun recalculatePossibleValues() {
        sudoku.allCells.forEach { cell -> cell.possibleValues() += sudoku.defaultValueSet }
        sudoku.allCells.filter { it.isSolved() }.forEach { it.recalculate() }
    }

    fun Group.getCellWithUniqueValue(): Pair<Cell, Int>? {
        val value = (1..9).firstOrNull{ v -> cellList.count { cell -> v in cell.possibleValues() } == 1}
        if (value != null)
            return Pair(cellList.first { cell -> value in cell.possibleValues() }, value)
        return null
    }

    private fun Cell.possibleValues(): MutableSet<Int> {
        return possibleCellValues[this]!!
    }

    private fun Cell.setValue(value: Int) {
        this.value = value
        this.recalculate()
    }

    private fun Cell.recalculate() {
        if (this.value != null) {
            this.possibleValues().clear()
            this.inGroups().forEach { group ->
                group.cellList.forEach { cell ->
                    cell.possibleValues() -= this.value!!
                }
            }
        }
    }

    private fun Cell.resetValue() {
        this.value = null
        this.possibleValues().clear()
    }

}

data class SolutionStep(val cell: Cell, val value: Int)

//1_631_690_923_660_279_808