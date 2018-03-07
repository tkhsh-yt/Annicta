package tkhshyt.annicta.pool

import tkhshyt.annict.json.Work

interface WorkPool {

    fun getWork(id: Long): Work?

    fun setWork(work: Work)

    fun setWorks(works: Collection<Work?>)

    fun updateWorkStatus(id: Long, status: String)

    fun containsWork(id: Long): Boolean
}