package tkhshyt.annict.json

import java.io.Serializable

/**
 * チャンネル情報を表すクラス．
 *
 * @property id チャンネルID．
 * @property name チャンネル名．
 */
data class Channel(
        val id  : Long,
        val name: String
): Serializable
