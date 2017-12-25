package tkhshyt.annict.json

import java.io.Serializable

/**
 * アクセストークン．
 *
 * @property access_token アクセストークン．
 * @property token_type
 * @property scope 権限．
 * @property created_at アクセストークンが作成された日時．
 */
data class AccessToken(
        val access_token: String,
        val token_type  : String,
        val scope       : String,
        val created_at  : Long
): Serializable
