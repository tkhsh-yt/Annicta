package tkhshyt.annict.json

import java.io.Serializable

/**
 * 作品の情報のページを表すクラス．
 *
 * @param works 作品情報のリスト．
 * @param totalCount ページを跨いだ全リソース数．
 * @param nextPage 次のページ数．
 * @param prevPage 前のページ数．
 */
data class Works(
        val works: List<Work>,
        val totalCount: Int,
        val nextPage: Int?,
        val prevPage: Int?
) : Serializable
