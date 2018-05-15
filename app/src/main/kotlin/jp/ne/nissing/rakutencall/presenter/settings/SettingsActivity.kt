package jp.ne.nissing.rakutencall.presenter.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import jp.ne.nissing.rakutencall.R

class SettingsActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        var fragment = supportFragmentManager.findFragmentById(R.id.base_fragment) as SettingsFragment?
        if (fragment == null) {
            fragment = SettingsFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.base_fragment, fragment).commit()
        }
    }

    companion object {
        fun launch(context: Context, flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.flags = flags
            context.startActivity(intent)
        }
    }
}
