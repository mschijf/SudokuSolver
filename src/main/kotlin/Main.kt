import ms.sudoku.SudokuStructureVijfling
import ms.sudoku.SudokuSolver
import ms.sudoku.SudokuStructureNormal
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos
import java.io.File
import kotlin.io.bufferedReader

fun main() {
//    solveBulkFile("easy.txt")
//    solveBulkFile("medium.txt")
//    solveBulkFile("hard.txt")
//    solveBulkFile("extreme.txt")

//    val inputLines = File("data/sudoku_lastig").bufferedReader().readLines()
//    val inputCellMap = inputLines.sudokuLinesToMap()
//    val sudoku = SudokuStructureNormal()
//    sudoku.initialFill(inputCellMap)

    val inputLines = File("data/sudoku_vijfling").bufferedReader().readLines()
    val inputCellMap = inputLines.sudokuLinesToMap()
    val sudoku = SudokuStructureVijfling()
    sudoku.initialFill(inputCellMap)


    val sudokuSolver = SudokuSolver(sudoku)
    sudoku.printSudoku()
    println("=====================================================")
//    sudokuSolver.solveRecursive()
    val x = sudokuSolver.countAll()
    println("totaal aantal oplossingen: $x")
    sudoku.printSudoku()
}

//0000183b305c 050703060007000800000816000000030000005000100730040086906000204840572093000409000  1.2
fun solveBulkFile(fileName: String) {
    println("-------------------------------------------------------------")
    println("start with $fileName")
    val inputLines = File("data/bulk/$fileName").bufferedReader().readLines()
    val startTime = System.currentTimeMillis()
    inputLines.forEachIndexed { index, line ->
        val sudokuString = line.split("\\s+".toRegex())[1]
        val sudokuLines = sudokuString.chunked(9)
        val inputCellMap = sudokuLines.sudokuLinesToMap()
        val sudoku = SudokuStructureNormal()
        sudoku.initialFill(inputCellMap)
        val sudokuSolver = SudokuSolver(sudoku)
        val count = sudokuSolver.countAll()
        if (count != 1L) {
            println ("$index has error")
        }
        if (index % 1000 == 0) {
            val timePassed = System.currentTimeMillis() - startTime
            println("$fileName ( $index ) in %d.%03d sec  ".format(timePassed / 1000, timePassed % 1000))
        }
    }
    val timePassed = System.currentTimeMillis() - startTime
    print("%d.%03d sec  ".format(timePassed / 1000, timePassed % 1000))
}

fun List<String>.sudokuLinesToMap():Map<Point, Int> {
    return this
        .flatMapIndexed { y, line ->
            line.padEnd(21, '.')
                .mapIndexed { x, ch ->  Pair(pos(x,y), if (ch.isDigit()) ch.digitToInt() else 0) }
        }
        .filter { it.second > 0 }
        .toMap()
}