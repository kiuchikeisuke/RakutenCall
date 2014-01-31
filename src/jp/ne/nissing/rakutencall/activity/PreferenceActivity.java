package jp.ne.nissing.rakutencall.activity;

import android.os.Bundle;

import jp.ne.nissing.rakutencall.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_screen);
    }
}
