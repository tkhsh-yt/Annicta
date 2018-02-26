package tkhshyt.annict.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * エピソード情報のページを表すクラス．
 *
 * @property resources エピソード情報のリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Episodes(
        val episodes: List<Episode>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
) : Serializable, Pagination<Episode> {

        override fun total_count(): Int {
                return total_count
        }

        override fun next_page(): Int? {
                return next_page
        }

        override fun prev_page(): Int? {
                return  prev_page
        }

        override fun resources(): List<Episode> {
                return episodes
        }
}
