package tkhshyt.annict.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * レビューのページを表すクラス．
 *
 * @property resources レビューのリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Reviews(
        val reviews: List<Review>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
) : Serializable, Pagination<Review> {

        override fun total_count(): Int {
                return total_count
        }

        override fun next_page(): Int? {
                return next_page
        }

        override fun prev_page(): Int? {
                return  prev_page
        }

        override fun resources(): List<Review> {
                return reviews
        }
}
