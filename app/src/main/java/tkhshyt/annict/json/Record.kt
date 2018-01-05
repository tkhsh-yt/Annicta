package tkhshyt.annict.json

import java.io.Serializable
import java.util.*

/**
 * エピソードへの記録を表すクラス．
 *
 * @property id 記録ID．
 * @property comment 記録したときに書かれた感想．
 * @property rating_state 記録したときに付けられたレーティング．bad，average，good，great の4種類が入っている．
 * @property is_modified 記録が編集されたかどうか．
 * @property likes_count Likeされた数．
 * @property comments_count コメントされた数．
 * @property created_at 記録された日時．
 * @property user 記録をした利用者の情報．
 * @property work 記録が紐づく作品情報．
 * @property episode 記録が紐づくエピソード情報．
 */
data class Record(
        val id: Long?,
        val comment: String?,
        val rating_state: String?,
        val is_modified: Boolean?,
        val likes_count: Int?,
        val comments_count: Int?,
        val created_at: Date?,
        val user: User?,
        val work: Work?,
        val episode: Episode?
) : Serializable
