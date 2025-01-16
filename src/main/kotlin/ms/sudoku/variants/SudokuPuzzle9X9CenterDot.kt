package ms.sudoku.variants

import ms.sudoku.base.Cell
import ms.sudoku.base.Group
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

class SudokuPuzzle9X9CenterDot: SudokuPuzzle9X9Base() {
    override fun makeExtraGroups(cellMap: Map<Point, Cell>): List<Group> {
        val group = Group(
            listOf(
                cellMap[pos(1, 1)]!!, cellMap[pos(4, 1)]!!, cellMap[pos(7, 1)]!!,
                cellMap[pos(1, 4)]!!, cellMap[pos(4, 4)]!!, cellMap[pos(7, 4)]!!,
                cellMap[pos(1, 7)]!!, cellMap[pos(4, 7)]!!, cellMap[pos(7, 7)]!!,            )
        )
        return listOf(group)
    }
}
