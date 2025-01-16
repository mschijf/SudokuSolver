package ms.sudoku.variants

import ms.sudoku.base.Cell
import ms.sudoku.base.Group
import tool.coordinate.twodimensional.Point

class SudokuPuzzle9X9: SudokuPuzzle9X9Base() {
    override fun makeExtraGroups(cellMap: Map<Point, Cell>): List<Group> {
        return emptyList()
    }
}