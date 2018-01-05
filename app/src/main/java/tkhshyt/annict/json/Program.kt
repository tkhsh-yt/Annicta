package tkhshyt.annict.json

import java.io.Serializable
import java.util.*

/**
 * 放送予定を表すクラス．
 *
 * @property id 放送予定ID．
 * @property started_at 放送開始日時．
 * @property is_rebroadcast 放送予定が再放送かどうか．再放送の場合は true が，そうでない場合は false が格納される．
 * @property channel チャンネル情報．
 * @property work 放送予定が紐づく作品情報．
 * @property episode 放送予定が紐づくエピソード情報．
 */
data class Program(
        val id: Long,
        val started_at: Date,
        val is_rebroadcast: Boolean,
        val channel: Channel,
        val work: Work,
        val episode: Episode
) : Serializable