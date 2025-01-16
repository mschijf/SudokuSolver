package ms.sudoku.variants

import ms.sudoku.base.Cell
import ms.sudoku.base.Group
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

class SudokuPuzzle9X9Cross: SudokuPuzzle9X9Base() {
    override fun makeExtraGroups(cellMap: Map<Point, Cell>): List<Group> {
        val diagonal1 = Group((0..8).map {x -> cellMap[pos(x,x)]!! })
        val diagonal2 = Group((0..8).map {x -> cellMap[pos(8-x,x)]!! })
        return listOf(diagonal1, diagonal2)
    }
}
