package tkhshyt.annict

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*
import tkhshyt.annict.json.*
import tkhshyt.annicta.BuildConfig

interface AnnictService {

    companion object {

        fun authorizeUrl(
                baseUrl: String,
                client_id: String,
                response_type: String = "code",
                redirect_uri: String = "urn:ietf:wg:oauth:2.0:oob",
                scope: String = "read write"
        ): String = "$baseUrl/oauth/authorize?client_id=$client_id&response_type=$response_type&redirect_uri=$redirect_uri&scope=$scope"
    }

    /**
     * アクセストークンを発行する．
     *
     * @param client_id クライアントID．
     * @param client_secret 作成したクライアントアプリケーションのシークレットキー．
     * @param grant_type authorization_code を指定する．
     * @param redirect_uri クライアントアプリケーションを作成したときに入力したコールバックURIを指定する．
     * @param code 認可後に取得した認証コードを指定する．
     * @return アクセストークン．
     */
    @POST("oauth/token")
    fun authorize(
            @Query("client_id") client_id: String = BuildConfig.CLIENT_ID,
            @Query("client_secret") client_secret: String = BuildConfig.CLIENT_SECRET,
            @Query("grant_type") grant_type: String = "authorization_code",
            @Query("redirect_uri") redirect_uri: String = "urn:ietf:wg:oauth:2.0:oob",
            @Query("code") code: String
    ): Single<Response<AccessToken>>

    /**
     * アクセストークンの情報を取得する．
     *
     * @param authorization Authorization ヘッダ．アクセストークンを渡す．(e.g. authorization="Authorization: Bearer ${access_token}")
     * @return
     */
    @GET("oauth/token/info")
    fun oauthTokenInfo(
            @Header("Authorization") authorization: String
    ): Single<Response<TokenInfo>>

    /**
     * アクセストークンを失効させる．
     *
     * @param authorization アクセストークン．
     * @param client_id クライアントID．
     * @param client_secret 作成したクライアントアプリケーションのシークレットキー．
     * @param token アクセストークン．
     * @return
     */
    @POST("oauth/revoke")
    fun revokeToken(
            @Header("Authorization") authorization: String,
            @Query("client_id") client_id: String = BuildConfig.CLIENT_ID,
            @Query("client_secret") client_secret: String = BuildConfig.CLIENT_SECRET,
            @Query("token") token: String
    ): Single<Response<Void>>

    /**
     * 作品情報を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_ids 作品を作品IDで絞り込む (e.g. fields=id,title)．
     * @param filter_season 作品を時期で絞り込む．2016-all としたときは，2016年にリリースされる作品全てを取得することができる (e.g. filter_season=2016-spring)．
     * @param filter_title 作品をタイトルで絞り込む (e.g. filter_title=shirobako)．
     * @param page ページ数を指定する (e.g. page=2)．
     * @param per_page 1ページに何件取得するか指定する．デフォルトは 25 件で，最大 50 件 (e.g. per_page=30)．
     * @param sort_id 作品を作品IDで並び替える．asc または desc を指定できる (e.g. sort_id = desc)．
     * @param sort_season 作品をリリース時期で並び替える．
     * @param sort_watchers_count 作品をWatchersの数で並び替える．asc または desc を指定できる (e.g. sort_watchers_count=desc)．
     * @return
     */
    @GET("{version}/works")
    fun works(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_season") filter_season: String? = null,
            @Query("filter_title") filter_title: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String? = null,
            @Query("sort_season") sort_season: String? = null,
            @Query("sort_watchers_count") sort_watchers_count: String? = null
    ): Single<Response<Works>>

    /**
     * エピソード情報を取得して返す．
     *
     * @param version
     * @param access_token
     * @param filter_ids
     * @param filter_work_id
     * @param page
     * @param per_page
     * @param sort_id
     * @param sort_sort_number
     * @return
     */
    @GET("{version}/episodes")
    fun episodes(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_work_id") filter_work_id: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String? = null,
            @Query("sort_sort_number") sort_sort_number: String = "desc"
    ): Single<Response<Episodes>>

    /**
     * エピソードへの記録を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む (e.g. fields=id,title)．
     * @param filter_ids 記録を記録IDで絞り込む (e.g. filter_ids=1,2,3)．
     * @param filter_episode_id 記録をエピソードIDで絞り込む (e.g. filter_episode_id=1111)．
     * @param filter_has_record_comment 記録を感想のあるなしで絞り込む．感想付きの記録のみ絞り込むときは true を，感想の無い記録のみを絞り込むときは false を指定する (e.g. filter_has_record_comment=true)．
     * @param page ページ数を指定する (e.g. page=2)．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる (e.g. per_page=30)．
     * @param sort_id 記録を記録IDで並び替える．asc または desc が指定できる (e.g. sort_id=desc)．
     * @param sort_likes_count 記録をLikeされた数で並び替える．asc または desc が指定できる (e.g. sort_likes_count=desc)．
     * @return
     */
    @GET("{version}/records")
    fun records(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_episode_id") filter_episode_id: Long? = null,
            @Query("filter_has_record_comment") filter_has_record_comment: Boolean? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String? = null,
            @Query("sort_likes_count") sort_likes_count: String? = null
    ): Single<Response<Records>>

    /**
     * 作品へのレビューを取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストーク
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_ids レビューをレビューIDで絞り込む．
     * @param filter_work_id レビューを作品IDで絞り込む．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる．
     * @param sort_id レビューをレビューIDで並び替える．asc または desc が指定できる．
     * @param sort_likes_count レビューをLikeされた数で並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/reviews")
    fun reviews(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_work_id") filter_work_id: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 20,
            @Query("sort_id") sort_id: String = "desc",
            @Query("sort_likes_count") sort_likes_count: String = "desc"
    ): Single<Response<Reviews>>

    /**
     * ユーザ情報を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_ids レビューをレビューIDで絞り込む．
     * @param filter_usernames ユーザをユーザ名で絞り込む．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる．
     * @param sort_id ユーザをIDで並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/users")
    fun users(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_usernames") filter_usernames: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String = "desc"
    ): Single<Response<Users>>

    /**
     * ユーザのフォロー情報を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_user_id ユーザIDで絞り込む．
     * @param filter_username ユーザ名で絞り込む．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．
     * @param sort_id IDで並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/following")
    fun following(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_user_id") filter_user_id: Long? = null,
            @Query("filter_username") filter_username: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String = "desc"
    ): Single<Response<Users>>

    /**
     * ユーザのフォロワー情報を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_user_id ユーザIDで絞り込む．
     * @param filter_username ユーザ名で絞り込む．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる．
     * @param sort_id IDで並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/followers")
    fun followers(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_user_id") filter_user_id: Long? = null,
            @Query("filter_username") filter_username: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String = "desc"
    ): Single<Response<Users>>

    /**
     * アクティビティを取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_user_id ユーザIDで絞り込む．
     * @param filter_username ユーザ名で絞り込む．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる．
     * @param sort_id IDで並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/activities")
    fun activities(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_user_id") filter_user_id: Long? = null,
            @Query("filter_username") filter_username: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String = "desc"
    ): Single<Response<Activities>>

    /**
     * 自分のプロフィール情報を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @return
     */
    @GET("{version}/me")
    fun profile(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null
    ): Single<Response<Profile>>

    /**
     * 作品のステータスを設定する．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param work_id 作品ID．
     * @param kind ステータスの種類．wanna_watch (見たい)，watching (見てる)，watched (見た)，on_hold (中断)，stop_watching (中止)，または no_select (未選択) が指定できる．
     * @return
     */
    @POST("{version}/me/statuses")
    fun updateState(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("work_id") work_id: Long,
            @Query("kind") kind: String
    ): Single<Response<Void>>

    /**
     * エピソードへの記録を作成する．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param episode_id エピソードID．
     * @param comment 感想．
     * @param rating_state レーティングの種類．bad，average，good，great が入力できる．
     * @param share_twitter 記録をTwitterにシェアするかどうか．true または false が入力できる．指定しなかったとき false になる．
     * @param share_facebook 記録をFacebookにシェアするかどうか．true または false が入力できる．指定しなかったとき false になる．
     * @return
     */
    @POST("{version}/me/records")
    fun createRecord(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("episode_id") episode_id: Long,
            @Query("comment") comment: String? = null,
            @Query("rating_state") rating_state: String? = null,
            @Query("share_twitter") share_twitter: Boolean = false,
            @Query("share_facebook") share_facebook: Boolean = false
    ): Single<Response<Record>>

    /**
     * 作成した記録を編集する．
     *
     * @param version 記録ID．
     * @param access_token アクセストークン．
     * @param id 記録ID．
     * @param comment 感想．
     * @param rating_state レーティングの種類．bad，average，good，great が入力できる．
     * @param share_twitter 記録をTwitterにシェアするかどうか．true または false が入力できる．指定しなかったときは false になる．
     * @param share_facebook 記録をFacebookにシェアするかどうか．true または false が入力できる．指定しなかったときは false になる．
     * @return
     */
    @PATCH("{version}/me/records/{id}")
    fun updateRecord(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Path("id") id: Long,
            @Query("comment") comment: String,
            @Query("rating") rating_state: String? = null,
            @Query("share_twitter") share_twitter: Boolean = false,
            @Query("share_facebook") share_facebook: Boolean = false
    ): Single<Response<Record>>

    /**
     * 作成した記録を削除する．
     *
     * @param version 初期状態 v1．
     * @param access_token アクセストークン．
     * @param id 記録ID．
     * @return
     */
    @DELETE("{version}/me/records/{id}")
    fun deleteRecord(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Path("id") id: Long
    ): Single<Response<Void>>

    /**
     * 自分がステータスを設定している作品の情報を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_ids 作品を作品IDを絞り込む．
     * @param filter_season 作品をリリース時期で絞り込む．2016-all としたときは，2016年にリリースされる作品全てを取得することができる．
     * @param filter_title 作品をタイトルを絞り込む．
     * @param filter_status 作品をステータスを絞り込む．wanna_watch，watching，watched，on_hold，stop_watching が指定できる．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50件まで指定できる．
     * @param sort_id 作品を作品IDで並び替える．asc または desc が指定できる．
     * @param sort_season 作品をリリース時期で並び替える．asc または desc が指定できる．
     * @param sort_watchers_count 作品をWatchersの数で並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/me/works")
    fun followingWorks(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_season") filter_season: String? = null,
            @Query("filter_title") filter_title: String? = null,
            @Query("filter_status") filter_status: String? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String? = null,
            @Query("sort_season") sort_season: String? = null,
            @Query("sort_watchers_count") sort_watchers_count: String? = null
    ): Single<Response<Works>>

    /**
     * 放送予定を取得して返す．
     *
     * @param version 初期値 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_ids 放送予定を放送予定IDで絞り込む．
     * @param filter_channel_ids 放送予定をチャンネルIDで絞り込む．
     * @param filter_work_ids 放送予定作品を作品IDで絞り込む．
     * @param filter_started_at_gt 放送予定を開始日時で絞り込む．指定した日付以降の放送予定が取得できる．なお，指定した日時のタイムゾーンはUTCとして解釈される．
     * @param filter_started_at_lt 放送予定を開始日時で絞り込む．指定した日付惟然の放送予定が取得できる．なお，指定した日時のタイムゾーンはUTCとして解釈される．
     * @param filter_unwatched 未紫朝の放送予定だけを取得する．
     * @param filter_rebroadcast 放送予定を再放送フラブをもとに絞り込む．true を渡すと再放送だけが，false を渡すと再放送以外の放送予定が取得できる．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる．
     * @param sort_id 放送予定を放送IDで並び替える．asc または desc が指定できる．
     * @param sort_started_at 放送予定を放送開始日時で並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/me/programs")
    fun programs(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String? = null,
            @Query("filter_ids") filter_ids: String? = null,
            @Query("filter_channel_ids") filter_channel_ids: String? = null,
            @Query("filter_work_ids") filter_work_ids: String? = null,
            @Query("filter_started_at_gt") filter_started_at_gt: String? = null,
            @Query("filter_started_at_lt") filter_started_at_lt: String? = null,
            @Query("filter_unwatched") filter_unwatched: Boolean = true,
            @Query("filter_rebroadcast") filter_rebroadcast: Boolean? = null,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String? = null,
            @Query("sort_started_at") sort_started_at: String = "desc"
    ): Single<Response<Programs>>

    /**
     * 自分がファローしているユーザのアクティビティを取得して返す．
     *
     * @param version 初期状態 v1．
     * @param access_token アクセストークン．
     * @param fields レスポンスボディに含まれるデータのフィールドを絞り込む．
     * @param filter_actions アクションで絞り込む．
     * @param filter_muted ミュート機能でミュートしているユーザを除外するかどうかを指定する．true で除外，false で除外しないようにできる．デフォルトは true (除外する)．
     * @param page ページ数を指定する．
     * @param per_page 1ページに何件取得するかを指定する．デフォルトは 25 件で，50 件まで指定できる．
     * @param sort_id IDで並び替える．asc または desc が指定できる．
     * @return
     */
    @GET("{version}/me/following_activities")
    fun followingActivities(
            @Path("version") version: String = "v1",
            @Query("access_token") access_token: String,
            @Query("fields") fields: String = "",
            @Query("filter_actions") filter_actions: String = "",
            @Query("filter_muted") filter_muted: Boolean = true,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 25,
            @Query("sort_id") sort_id: String = "desc"
    ): Single<Response<Activities>>
}
