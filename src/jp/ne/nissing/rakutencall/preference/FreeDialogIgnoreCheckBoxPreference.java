package jp.ne.nissing.rakutencall.preference;

import jp.ne.nissing.rakutencall.R;
import android.content.Context;
import android.preference.*;
import android.util.AttributeSet;
import android.widget.Toast;

public class FreeDialogIgnoreCheckBoxPreference extends CheckBoxPreference {

    private Context mContext = null;
    
    public FreeDialogIgnoreCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.KEY_FREE_DIAL);
        
        init();
    }

    private void init() {
        //サマリーの設定
        this.setSummaryOn(R.string.checkbox_free_dial_summary_on);
        this.setSummaryOff(R.string.checkbox_free_dial_summary_off);
        
        //デフォルト値の設定
        this.setChecked(SharedPreferenceManager.getInstance(mContext).getFreeDialIgnoreCheck());
        
        //リスナーの設定
        this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(newValue instanceof Boolean == false){
                    return false;
                }
                Boolean isChecked = (Boolean) newValue;
                
                SharedPreferenceManager.getInstance(mContext).setFreeDialIgnoreCheck(isChecked);
                if(isChecked){
                    Toast.makeText(mContext, R.string.toast_free_dial_on, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(mContext,R.string.toast_free_dial_off, Toast.LENGTH_LONG).show();
                }
                
                return true;
            }
        });
    }
    
    
}