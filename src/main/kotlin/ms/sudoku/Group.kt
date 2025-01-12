package ms.sudoku

data class Group(
    val cellList: List<Cell>) {

    fun hasCell(cell: Cell): Boolean {
        return cell in cellList
    }

    fun hasUnqiueCellValue(): Pair<Cell, Int>? {
        cellList.filter { cell -> cell.isNotSolved() }.forEach { cell ->
            cell.possibleValues.forEach { aValue ->
                val found = cellList.filter { it != cell }.none { aValue in it.possibleValues }
                if (found) {
                    return Pair(cell, aValue)
                }
            }
        }
        return null
    }

    fun verifyOk(): Boolean {
        return cellList.all{it.isSolved()} && cellList.map{it.value}.distinct().size == cellList.size
    }

    override fun toString(): String {
        return cellList.joinToString() { it.pos.toString() }
    }
}