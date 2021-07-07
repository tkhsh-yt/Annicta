package tkhshyt.annicta.work_info

import tkhshyt.annict.json.Episode
import tkhshyt.annict.json.Work

interface WorkInfoItem

data class WorkItem(
        val work: Work
): WorkInfoItem

data class EpisodeItem(
        val episode: Episode
): WorkInfoItem