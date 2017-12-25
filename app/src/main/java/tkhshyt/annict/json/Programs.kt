package tkhshyt.annict.json

import java.io.Serializable

/**
 * 放送予定情報のページを表すクラス．
 *
 * @property programs 放送予定情報のリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Programs(
        val programs  : List<Program>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
): Serializable
