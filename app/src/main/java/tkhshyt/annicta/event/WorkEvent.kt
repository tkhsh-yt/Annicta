package tkhshyt.annicta.event

/**
 * 視聴状況を変更するためのイベント．
 *
 * @param workId 変更対象となった作品
 * @param status 変更後の状況
 */
data class UpdateStatusEvent(val workId: Long, val status: String)