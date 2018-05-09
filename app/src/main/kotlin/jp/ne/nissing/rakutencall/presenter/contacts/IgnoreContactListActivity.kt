package jp.ne.nissing.rakutencall.presenter.contacts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import dagger.android.support.DaggerAppCompatActivity
import jp.ne.nissing.rakutencall.R
import jp.ne.nissing.rakutencall.presenter.settings.SettingsActivity

class IgnoreContactListActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)
        supportActionBar!!.setIcon(R.drawable.ic_launcher_small)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        var fragment = supportFragmentManager.findFragmentById(R.id.base_fragment) as IgnoreContactListFragment?
        if (fragment == null) {
            fragment = IgnoreContactListFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.base_fragment, fragment).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_setting -> SettingsActivity.launch(this)
            R.id.menu_privacy_policy -> jumpToUri(getString(R.string.privacy_policy_url).toUri())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun jumpToUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
        }
        startActivity(intent)
    }

    companion object {
        fun launch(context: Context, flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK) {
            val intent = Intent(context, IgnoreContactListActivity::class.java)
            intent.flags = flags
            context.startActivity(intent)
        }
    }
}
