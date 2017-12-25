package tkhshyt.annict.json

import java.io.Serializable

/**
 * 作品情報を表すクラス．
 *
 * @property id 作品のID．
 * @property title 作品のタイトル．
 * @property title_kana 作品タイトルの読み仮名．
 * @property media リリース媒体．
 * @property media_text リリース媒体 (表記用)．
 * @property season_name リリース時期．
 * @property season_name_text リリース時期 (表記用)．
 * @property released_on リリース日．
 * @property released_on_about 未確定な大体のリリース日．
 * @property official_site_url 公式サイトのURL．
 * @property wikipedia_url WikipediaのURL．
 * @property twitter_username 公式サイトのURL．
 * @property twitter_hashtag Twitterの作品に関するハッシュタグ．
 * @property images 画像情報．
 * @property episodes_count エピソード数．
 * @property watchers_count 見てる/見たい/見た人の数．
 * @property reviews_count レビュー数．
 * @property no_episodes エピソードが存在しない作品かどうか．例えば映画はエピソードが存在しない作品なので，true になる．
 * @property status ステータスの種類．
 */
data class Work(
        val id               : Long?,
        val title            : String?,
        val title_kana       : String?,
        val media            : String?,
        val media_text       : String?,
        val season_name      : String?,
        val season_name_text : String?,
        val released_on      : String?,
        val released_on_about: String?,
        val official_site_url: String?,
        val wikipedia_url    : String?,
        val twitter_username : String?,
        val twitter_hashtag  : String?,
        val images           : Images?,
        val episodes_count   : Int?,
        val watchers_count   : Int?,
        val reviews_count    : Int?,
        val no_episodes      : Boolean?,
        val status           : Status?
) : Serializable