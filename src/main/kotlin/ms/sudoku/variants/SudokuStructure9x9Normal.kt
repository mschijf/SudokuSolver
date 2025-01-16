package ms.sudoku.variants

import ms.sudoku.base.Cell
import ms.sudoku.base.Group
import tool.coordinate.twodimensional.Point

class SudokuStructure9x9Normal: SudokuStructure9x9Base() {
    override fun makeExtraGroups(cellMap: Map<Point, Cell>): List<Group> {
        return emptyList()
    }
}