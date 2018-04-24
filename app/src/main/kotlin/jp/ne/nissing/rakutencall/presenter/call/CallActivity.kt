package jp.ne.nissing.rakutencall.presenter.call

import android.content.Context
import android.content.Intent
import android.os.Bundle
import jp.ne.nissing.rakutencall.debug.R
import dagger.android.support.DaggerAppCompatActivity

class CallActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        var fragment = supportFragmentManager.findFragmentById(R.id.base_fragment) as CallFragment?
        if (fragment == null) {
            fragment = CallFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.base_fragment, fragment).commit()
        }
    }

    companion object {
        fun launch(context: Context, flags:Int = Intent.FLAG_ACTIVITY_NEW_TASK) {
            val intent = Intent(context, CallActivity::class.java)
            intent.flags = flags
            context.startActivity(intent)
        }
    }
}
