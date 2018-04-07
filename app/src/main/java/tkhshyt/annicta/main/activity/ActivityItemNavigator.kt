package tkhshyt.annicta.main.activity

import tkhshyt.annict.json.Work

interface ActivityItemNavigator {

    fun onItemClick(work: Work)
}