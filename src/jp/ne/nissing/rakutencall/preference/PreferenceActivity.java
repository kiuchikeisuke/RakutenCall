package jp.ne.nissing.rakutencall.preference;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import android.os.Bundle;
import android.widget.Toast;

import jp.ne.nissing.rakutencall.R;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_screen);
        
        AdBuddiz.cacheAds(this);
    }

    private void showAdBuddiz() {
        AdBuddiz.showAd(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if(SharedPreferenceManager.getInstance(this).getIsAdbuddizHide() == false){
            showAdBuddiz();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdBuddiz.onDestroy();
    }
}
