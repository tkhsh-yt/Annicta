package tkhshyt.annicta.work_info

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import tkhshyt.annict.json.Work
import tkhshyt.annicta.AnnictApplication

class WorkInfoItemViewModel(
        context: Application
): AndroidViewModel(context) {

    val work = MutableLiveData<Work>()

    fun openTwitter(view: View) {
        val url = "https://twitter.com/${work.value?.twitter_username}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        getApplication<Application>().startActivity(intent)
    }

    fun openTwitterHashtag(view: View) {
        val url = "https://twitter.com/search?q=${work.value?.twitter_hashtag}&src=hash"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        getApplication<Application>().startActivity(intent)
    }

    fun openWikipedia(view: View) {
        val url = work.value?.wikipedia_url
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        getApplication<Application>().startActivity(intent)
    }

    fun openMyAnimeList(view: View) {
        val url = "https://myanimelist.net/anime/${work.value?.mal_anime_id}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        getApplication<Application>().startActivity(intent)
    }
}