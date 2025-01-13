package ms.sudoku

class Group(
    val cellList: List<Cell>) {

    fun hasCell(cell: Cell): Boolean {
        return cell in cellList
    }

    override fun toString(): String = cellList.joinToString() { it.pos.toString() }
}