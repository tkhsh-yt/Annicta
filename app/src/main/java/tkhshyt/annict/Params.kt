package tkhshyt.annict

import retrofit2.http.Path
import retrofit2.http.Query

interface Params {

    class Programs internal constructor(
            val version: String = "v1",
            val fields: String? = null,
            val filter_ids: String? = null,
            val filter_channel_ids: String? = null,
            val filter_work_ids: String? = null,
            val filter_started_at_gt: String? = null,
            val filter_started_at_lt: String? = null,
            val filter_unwatched: Boolean = true,
            val filter_rebroadcast: Boolean? = null,
            val page: Int = 1,
            val per_page: Int = 25,
            val sort_id: String? = null,
            val sort_started_at: String = "desc"
    )

    class ProgramsBuilder() {
        private var version: String = "v1"
        private var fields: String? = null
        private var filter_ids: String? = null
        private var filter_channel_ids: String? = null
        private var filter_work_ids: String? = null
        private var filter_started_at_gt: String? = null
        private var filter_started_at_lt: String? = null
        private var filter_unwatched: Boolean = true
        private var filter_rebroadcast: Boolean? = null
        private var page: Int = 1
        private var per_page: Int = 25
        private var sort_id: String? = null
        private var sort_started_at: String = "desc"

        fun version(version: String): ProgramsBuilder {
            this.version = version
            return this
        }

        fun fields(fields: String): ProgramsBuilder {
            this.fields = fields
            return this
        }

        fun page(page: Int): ProgramsBuilder {
            this.page = page
            return this
        }

        fun build(): Programs {
            return Programs(
                    version, fields, filter_ids, filter_channel_ids,
                    filter_work_ids, filter_started_at_gt,
                    filter_started_at_lt, filter_unwatched, filter_rebroadcast,
                    page, per_page, sort_id, sort_started_at
            )
        }
    }

}