package tkhshyt.annict

enum class Kind(val kind: String) {
    NoSelect("no_select"),
    WannaWatch("wanna_watch"),
    Watching("watching"),
    Watched("watched"),
    OnHold("on_hold"),
    StopWatching("stop_watching");

    companion object {
        fun stringToIndex(string: String?): Int {
            return Kind.values().indexOfFirst { it.kind == string }
        }
    }
}