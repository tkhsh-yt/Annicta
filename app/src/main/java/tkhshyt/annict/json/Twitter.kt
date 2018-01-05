package tkhshyt.annict.json

import java.io.Serializable

/**
 * Twitterアカウントの情報
 *
 * @property mini_avatar_url Twitterアカウントのアバター画像 (mini)．
 * @property normal_avatar_url Twitterアカウントのアバター画像 (normal)．
 * @property bigger_avatar_url Twitterアカウントのアバター画像 (bigger)．
 * @property original_avatar_url Twitterアカウントのアバター画像 (original)．
 * @property image_url official_site_user のページで取得できる twitter:image のURL．
 */
data class Twitter(
        val mini_avatar_url: String,
        val normal_avatar_url: String,
        val bigger_avatar_url: String,
        val original_avatar_url: String,
        val image_url: String
) : Serializable