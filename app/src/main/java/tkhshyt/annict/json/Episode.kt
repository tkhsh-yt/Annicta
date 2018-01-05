package tkhshyt.annict.json

import java.io.Serializable

/**
 * エピソード情報を表すクラス．
 *
 * @property id エピソードID．
 * @property number エピソードの話数．
 * @property number_text エピソードの話数(表記用)．
 * @property sort_number ソート用の番号．
 * @property title サブタイトル．
 * @property records_count 記録数．
 * @property record_comments_count 感想付きの記録数．
 * @property work エピソードが紐づく作品情報．
 * @property prev_episode 前のエピソードの情報．
 * @property next_episode 次のエピソードの情報．
 */
data class Episode(
        val id: Long?,
        val number: String?,
        val number_text: String?,
        val sort_number: Int?,
        val title: String?,
        val records_count: Int?,
        val record_comments_count: Int?,
        val work: Work?,
        val prev_episode: Episode?,
        val next_episode: Episode?
) : Serializable
