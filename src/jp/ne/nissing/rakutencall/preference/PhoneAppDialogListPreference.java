package jp.ne.nissing.rakutencall.preference;

import java.util.List;

import jp.ne.nissing.rakutencall.preference.phoneappdata.*;
import android.app.AlertDialog.Builder;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

public class PhoneAppDialogListPreference extends ListPreference {

    private Context mContext = null;
    
    public PhoneAppDialogListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.KEY_PACKAGE);
        init();
    }
    
    private void init(){
        for(PhoneActivityData data : PhoneAppUtil.getActivitis(mContext)){
            if(data.isSelected()){
                setSummary(data);
                break;
            }
        }
    }
    
    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        int defaultPhoneAppIndex = 0;
        
        final List<PhoneActivityData> lists = PhoneAppUtil.getActivitis(mContext);
        
        for(int i = 0 ; i < lists.size(); i++){
            if(lists.get(i).isSelected()){
                defaultPhoneAppIndex = i;
                break;
            }
        }
        
        //フェールセーフとしてもしリストがチェックされない場合は一番最初の電話アプリを選択状態にする
        if(defaultPhoneAppIndex == -1 && lists.size() > 0){
            lists.get(0).setSelected(true);
            defaultPhoneAppIndex = 0;
            PhoneActivityData phoneActivityData = lists.get(0);
            SharedPreferenceManager.getInstance(mContext).setDefaultPhoneApp(
                    phoneActivityData.getPackageName(), phoneActivityData.getAcitivityName());
            setSummary(phoneActivityData);
        }
        
        PhoneActivityDataAdapter adapter = new PhoneActivityDataAdapter(mContext, 0, lists);
        builder.setSingleChoiceItems(adapter, defaultPhoneAppIndex, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneActivityData phoneActivityData = lists.get(which);
                SharedPreferenceManager.getInstance(mContext).setDefaultPhoneApp(
                        phoneActivityData.getPackageName(), phoneActivityData.getAcitivityName());
                setSummary(phoneActivityData);
                dialog.cancel();
            }
        });
        builder.setPositiveButton(null, null);
    }
    
    private void setSummary(PhoneActivityData data){
        if(TextUtils.isEmpty(data.getApplicationName()) == false){
            this.setSummary(data.getApplicationName());
        }
    }
}
