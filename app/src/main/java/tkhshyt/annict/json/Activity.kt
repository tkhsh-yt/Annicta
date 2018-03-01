package tkhshyt.annict.json

import java.io.Serializable
import java.util.*

/**
 * アクティビティを表すクラス．
 *
 * @property id アクティビティID．
 * @property user ユーザ情報．
 * @property work 作品情報．
 * @property action アクティビティの種類．create_record (記録)，create_multiple_records (一括記録)，create_status (ステータス変更) の3種類が存在する．
 * @property created_at アクティビティ作成日時．
 * @property episode 記録されたエピソード情報．
 * @property record 記録情報．
 * @property review レビュー情報．
 * @property multiple_records 記録されたエピソード情報．
 * @property status 記録情報．
 */
data class Activity(
        val id: Long?,
        val user: User?,
        val work: Work?,
        val action: String?,
        val created_at: Date?,
        // status: create_record
        val episode: Episode?,
        val record: Record?,
        // status: create_review
        val review: Review?,
        // status: create_multi_records
        val multiple_record: List<MultipleRecord>?,
        // status: create_status
        val status: Status?
) : Serializable

