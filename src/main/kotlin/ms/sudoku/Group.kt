package ms.sudoku

class Group(
    val cellList: List<Cell>) {

    fun hasCell(cell: Cell): Boolean {
        return cell in cellList
    }

    fun getCellWithUniqueValue(): Pair<Cell, Int>? {
        val value = (1..9).firstOrNull{ v -> cellList.count { v in it.possibleValues } == 1}
        if (value != null)
            return Pair(cellList.first { value in it.possibleValues }, value)
        return null
    }

    fun verifyOk(): Boolean {
        return cellList.all{it.isSolved()} && cellList.map{it.value}.distinct().size == cellList.size
    }

    override fun toString(): String {
        return cellList.joinToString() { it.pos.toString() }
    }
}