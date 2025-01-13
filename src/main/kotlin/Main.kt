package ms

import ms.ms.sudoku.SudokuStructure
import ms.ms.sudoku.SudokuType
import ms.sudoku.SudokuSolver
import tool.coordinate.twodimensional.pos
import java.io.File
import kotlin.io.bufferedReader

fun main() {
    val sudokuType = SudokuType.NORMAL
    val inputLines = File("data/sudoku_extreme_2025012").bufferedReader().readLines()
    val inputCellMap = inputLines
        .flatMapIndexed { y, line ->
            line.padEnd(21, '.')
                .mapIndexed { x, ch ->  Pair(pos(x,y), if (ch.isDigit()) ch.digitToInt() else 0) }
        }
        .filter { it.second > 0 }
        .toMap()

    val sudoku = SudokuStructure(sudokuType)
    sudoku.initialFill(inputCellMap)
    val sudokuSolver = SudokuSolver(sudoku)
    sudoku.printSudoku()
    sudokuSolver.solve()
    sudoku.printSudoku()
    sudokuSolver.solveRecursive()
    sudoku.printSudoku()
}