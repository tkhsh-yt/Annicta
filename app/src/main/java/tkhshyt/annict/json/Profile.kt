package tkhshyt.annict.json

import java.io.Serializable
import java.util.*

/**
 * プロフィール情報を表すクラス．
 *
 * @property id ユーザID．
 * @property username URLなどで使用されているユーザ名．
 * @property name ユーザの名前．
 * @property description プロフィール．
 * @property url ユーザのURL．
 * @property avatar_url アバター画像．
 * @property background_image_url 背景画像．
 * @property records_count 記録数．
 * @property followings_count ユーザがフォローしている人の数．
 * @property followers_count ユーザをフォローしている人の数．
 * @property wanna_watch_count ステータスを「見たい」にしている作品の数．
 * @property watching_count ステータスを「見てる」にしている作品の数．
 * @property watched_count ステータスを「見た」にしている作品の数．
 * @property on_hold_count ステータスを「中断」にしている作品の数．
 * @property stop_watching_count ステータスを「中止」にしている作品の数．
 * @property created_at ユーザ登録した日時．
 * @property email ユーザのメールアドレス．
 * @property notifications_count 通知数．
 */
data class Profile(
        val id                  : Long?,
        val username            : String?,
        val name                : String?,
        val description         : String?,
        val url                 : String?,
        val avatar_url          : String?,
        val background_image_url: String?,
        val records_count       : Int?,
        val followings_count    : Int?,
        val followers_count     : Int?,
        val wanna_watch_count   : Int?,
        val watching_count      : Int?,
        val watched_count       : Int?,
        val on_hold_count       : Int?,
        val stop_watching_count : Int?,
        val created_at          : Date?,
        val email               : String?,
        val notifications_count : Int?
): Serializable
