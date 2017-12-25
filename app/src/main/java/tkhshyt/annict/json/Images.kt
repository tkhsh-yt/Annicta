package tkhshyt.annict.json

import java.io.Serializable

/**
 * 画像情報を表すクラス．
 *
 * @property facebook Facebookに関する情報．
 * @property twitter Twitterに関する情報．
 * @property recommended_url facebook.og_image_url，twitter.bigger_avatar_url，twitter_image_url のうち，解像度が一番大きい画像のURL．扱いやすい画像のURLが高確率で格納されている．
 */
data class Images(
        val facebook       : Facebook,
        val twitter        : Twitter,
        val recommended_url: String
): Serializable