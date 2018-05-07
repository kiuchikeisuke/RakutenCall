package jp.ne.nissing.rakutencall.presenter.call

import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import jp.ne.nissing.rakutencall.R

class CallActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity)

        if (intent == null || intent.data == null) {
            finish()
            return
        }

        var fragment = supportFragmentManager.findFragmentById(R.id.base_fragment) as CallFragment?
        if (fragment == null) {
            fragment = CallFragment.newInstance(intent.data.toString())
            supportFragmentManager.beginTransaction().add(R.id.base_fragment, fragment).commit()
        }
    }
}
