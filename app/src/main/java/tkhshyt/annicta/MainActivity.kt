package tkhshyt.annicta

import android.content.ContextWrapper
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.pixplicity.easyprefs.library.Prefs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()

        
    }
}
