package tkhshyt.annict.json

interface Pagination<T> {
    fun total_count(): Int
    fun next_page(): Int?
    fun prev_page(): Int?
    fun resources(): List<T>
}