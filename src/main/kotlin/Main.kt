package ms

import ms.sudoku.SudokuVijfling
import java.io.File
import kotlin.io.bufferedReader

fun main() {
    val inputLines = File("data/sudoku").bufferedReader().readLines()
    val sudoku = SudokuVijfling.of(inputLines)
    sudoku.printSudoku()
    sudoku.solve()
    sudoku.printSudoku()
}