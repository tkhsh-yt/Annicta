package tkhshyt.annicta.event

import tkhshyt.annict.json.Episode
import tkhshyt.annict.json.Record

data class StartRecordActivityEvent(val episode: Episode)

/**
 * 視聴を記録したときのイベント．
 */
data class RecordedEvent(val record: Record)