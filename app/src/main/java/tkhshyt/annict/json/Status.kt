package tkhshyt.annict.json

import java.io.Serializable

/**
 * 作品のステータスを表すクラス．
 *
 * @property kind ステータスの種類．wanna_watch (見たい)，watching (見てる)，watched (見た)，on_hold (中断)，stop_watching (中止)，または no_select (未選択) が入る．
 */
data class Status(
        val kind: String
) : Serializable