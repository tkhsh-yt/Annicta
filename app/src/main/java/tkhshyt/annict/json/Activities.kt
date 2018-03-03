package tkhshyt.annict.json

import java.io.Serializable

/**
 * アクティビティのページを表すクラス．
 *
 * @property resources アクティビティのリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Activities(
        val activities: List<Activity>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
) : Serializable, Pagination<Activity> {

    override fun total_count(): Int {
        return total_count
    }

    override fun next_page(): Int? {
        return next_page
    }

    override fun prev_page(): Int? {
        return prev_page
    }

    override fun resources(): List<Activity> {
        return activities
    }
}
