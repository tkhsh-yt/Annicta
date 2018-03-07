package tkhshyt.annicta.pool

import tkhshyt.annict.json.Status
import tkhshyt.annict.json.Work

class WorkPoolMap : WorkPool {

    private var workPool: MutableMap<Long, Work> = mutableMapOf()

    override fun getWork(id: Long): Work? {
        val work = workPool[id]
        if (work != null) {
            return work
        }
        workPool = mutableMapOf()
        return null
    }

    override fun setWork(work: Work) {
        if (work.id != null) {
            val id = work.id
            if (work.status == null && containsWork(id)) {
                val old = getWork(id)
                if (old?.status != null) {
                    workPool[id] = work.copy(status = old.status)
                    return
                }
            }
            workPool[id] = work
        }
    }

    override fun setWorks(works: Collection<Work?>) {
        works.forEach {
            if (it != null) {
                setWork(it)
            }
        }
    }

    override fun updateWorkStatus(id: Long, status: String) {
        if (containsWork(id)) {
            val work = getWork(id)
            work?.copy(status = Status(status))?.let {
                setWork(it)
            }
        }
    }

    override fun containsWork(id: Long): Boolean {
        return workPool.contains(id)
    }
}