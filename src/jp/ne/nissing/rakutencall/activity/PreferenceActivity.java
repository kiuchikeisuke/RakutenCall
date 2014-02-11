package jp.ne.nissing.rakutencall.activity;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.os.Bundle;
import android.widget.Toast;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_screen);
        
        AdBuddiz.cacheAds(this);
        if(SharedPreferenceManager.getInstance(this).getIsAdbuddizHide() == false){
            showAdBuddiz();
        }
    }

    private void showAdBuddiz() {
        AdBuddiz.showAd(this);
        Toast.makeText(this,R.string.toast_adbuddiz_show,Toast.LENGTH_LONG).show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdBuddiz.onDestroy();
    }
}
