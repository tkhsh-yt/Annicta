package tkhshyt.annict.json

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 放送予定情報のページを表すクラス．
 *
 * @property resources 放送予定情報のリスト．
 * @property total_count ページを跨いだ全リソース数．
 * @property next_page 次のページ数．
 * @property prev_page 前のページ数．
 */
data class Programs(
        val programs: List<Program>,
        val total_count: Int,
        val next_page: Int?,
        val prev_page: Int?
) : Serializable, Pagination<Program> {

        override fun total_count(): Int {
                return total_count
        }

        override fun next_page(): Int? {
                return next_page
        }

        override fun prev_page(): Int? {
                return  prev_page
        }

        override fun resources(): List<Program> {
                return programs
        }
}

