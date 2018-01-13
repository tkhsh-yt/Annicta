package tkhshyt.annicta.event

import tkhshyt.annict.json.Episode

data class ShowRecordDialogEvent(val episode: Episode)

data class CreateRecord(
        val episode: Episode,
        val rating_state: String?,
        val comment: String?,
        val share_twitter: Boolean,
        val share_facebook: Boolean
)

data class CreateRecordEvent(val createRecord: CreateRecord)