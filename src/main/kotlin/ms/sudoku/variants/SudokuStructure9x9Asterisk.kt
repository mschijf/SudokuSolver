package ms.sudoku.variants

import ms.sudoku.base.Cell
import ms.sudoku.base.Group
import tool.coordinate.twodimensional.Point
import tool.coordinate.twodimensional.pos

class SudokuStructure9x9Asterisk: SudokuStructure9x9Base() {
    override fun makeExtraGroups(cellMap: Map<Point, Cell>): List<Group> {
        val group = Group(
            listOf(
                cellMap[pos(2, 2)]!!, cellMap[pos(4, 1)]!!, cellMap[pos(6, 2)]!!,
                cellMap[pos(1, 4)]!!, cellMap[pos(4, 4)]!!, cellMap[pos(7, 4)]!!,
                cellMap[pos(2, 6)]!!, cellMap[pos(4, 7)]!!, cellMap[pos(6, 6)]!!
            )
        )
        return listOf(group)
    }
}
