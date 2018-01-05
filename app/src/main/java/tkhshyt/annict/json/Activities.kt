package tkhshyt.annict.json

import java.io.Serializable

/**
 * アクティビティのページを表すクラス．
 *
 * @property activities アクティビティのリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Activities(
        val activities: List<Activity>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
) : Serializable