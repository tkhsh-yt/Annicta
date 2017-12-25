package tkhshyt.annict.json

import java.io.Serializable

/**
 * エピソード情報のページを表すクラス．
 *
 * @property episodes エピソード情報のリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Episodes(
        val episodes  : List<Episode>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
): Serializable