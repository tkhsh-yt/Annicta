package tkhshyt.annict.json

import java.io.Serializable

/**
 * 複数記録された記録情報を表すクラス．
 *
 * @property episode 記録されたエピソード情報．
 * @property record 記録情報．
 */
data class MultipleRecord(
        val episode: Episode,
        val record: Record
) : Serializable
