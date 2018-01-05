package tkhshyt.annict.json

import java.io.Serializable

/**
 * アクセストークン情報を表すクラス．
 *
 * @property resource_owner_id
 * @property scope 権限．
 * @property expires_in_seconds トークン失効までの時間．
 * @property application
 * @property created_at
 */
data class TokenInfo(
        val resource_owner_id: Int,
        val scope: List<String>,
        val expires_in_seconds: Int?,
        val application: Application,
        val created_at: Long
) : Serializable