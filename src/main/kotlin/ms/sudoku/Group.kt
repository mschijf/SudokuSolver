package ms.sudoku

data class Group(
    val cellList: List<Cell>) {

    fun hasCell(cell: Cell): Boolean = (cell in cellList)
    override fun toString(): String = cellList.joinToString() { it.pos.toString() }
}