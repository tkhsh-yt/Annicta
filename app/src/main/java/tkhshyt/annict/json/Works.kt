package tkhshyt.annict.json

import java.io.Serializable

/**
 * 作品の情報のページを表すクラス．
 *
 * @param works 作品情報のリスト．
 * @param total_count ページを跨いだ全リソース数．
 * @param next_page 次のページ数．
 * @param prev_page 前のページ数．
 */
data class Works(
        val works: List<Work>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
) : Serializable
