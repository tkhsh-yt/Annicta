package tkhshyt.annict.json

import java.io.Serializable

/**
 * Facebookの情報を表すクラス．
 *
 * @property og_image_url official_site_url のページで取得できる og:image のURL
 */
data class Facebook(
        val og_image_url: String
) : Serializable