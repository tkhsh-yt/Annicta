package tkhshyt.annict.json

import java.io.Serializable

/**
 * レビューのページを表すクラス．
 *
 * @property reviews レビューのリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Reviews(
        val reviews    : List<Review>,
        val total_count: Int,
        val next_page  : Int?,
        val prev_page  : Int?
) : Serializable