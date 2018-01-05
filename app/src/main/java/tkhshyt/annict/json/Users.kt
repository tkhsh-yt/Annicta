package tkhshyt.annict.json

import java.io.Serializable

/**
 * ユーザ．
 *
 * @property users ユーザの配列
 * @property totalCount ページを跨いだ全リソース数
 * @property nextPage 次のページ数
 * @property prevPage 前のページ数
 */
data class Users(
        val users: List<User>,
        val totalCount: Int,
        val nextPage: Int?,
        val prevPage: Int?
) : Serializable