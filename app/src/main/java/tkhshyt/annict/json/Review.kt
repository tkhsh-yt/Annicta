package tkhshyt.annict.json

import java.io.Serializable
import java.util.*

/**
 * 作品へのレビューを表すクラス．
 *
 * @property id レビューID．
 * @property body レビュー本文．
 * @property rating_animation_state レビューしたときに付けられたレーティング (作画)．bad，average，good，great の4種類のうち1つが入っている．
 * @property rating_music_state レビューしたときに付けられたレーティング (音楽)．bad，average，good，great の4種類のうち1つが入っている．
 * @property rating_story_state レビューしたときに付けられたレーティング (ストーリー)．bad，average，good，great の4種類のうち1つが入っている．
 * @property rating_character_state レビューしたときに付けられたレーティング (キャラクター)．bad，average，good，great の4種類のうち1つが入っている．
 * @property rating_overall_state レビューしたときに付けられたレーティング (全体)．bad，average，good，average の4種類のうち1つが入っている．
 * @property likes_count Likeされた数．
 * @property impressions_count PV数．
 * @property created_at レビューが投稿された日時．
 * @property modified_at レビューが編集された日時．
 * @property user このレビューを投稿した利用者の情報．
 * @property work このレビューが紐付く作品情報．
 */
data class Review(
        val id                    : Long?,
        val body                  : String?,
        val rating_animation_state: String?,
        val rating_music_state    : String?,
        val rating_story_state    : String?,
        val rating_character_state: String?,
        val rating_overall_state  : String?,
        val likes_count           : Int?,
        val impressions_count     : Int?,
        val created_at            : Date?,
        val modified_at           : Date?,
        val user                  : User?,
        val work                  : Work?
) : Serializable