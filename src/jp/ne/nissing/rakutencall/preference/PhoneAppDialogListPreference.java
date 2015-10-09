package jp.ne.nissing.rakutencall.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

import jp.ne.nissing.rakutencall.preference.phoneappdata.*;

import java.util.ArrayList;
import java.util.List;

public class PhoneAppDialogListPreference extends ListPreference {

    private Context mContext = null;
    private static final String RAKUDEN_PACAGE_NAME = "jp.ne.nissing.rakutencall";
    
    public PhoneAppDialogListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.KEY_PACKAGE);
        init();
    }
    
    private void init(){
        for(PhoneActivityData data : getActivitis()){
            if(data.isSelected()){
                setSummary(data);
                break;
            }
        }
    }
    
    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        int defaultPhoneAppIndex = 0;
        
        final List<PhoneActivityData> lists = getActivitis();
        
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
    
    private List<PhoneActivityData> getActivitis(){
        List<PhoneActivityData> lists = new ArrayList<PhoneActivityData>();

        PackageManager pm = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        PhoneActivityData defaultPhoneAppData = SharedPreferenceManager.getInstance(mContext)
                .getDefaultPhoneApp();

        for (ResolveInfo act : activities) {
            String label = act.loadLabel(pm).toString();
            Drawable icon = act.loadIcon(pm);
            String packageName = act.activityInfo.packageName;
            String activityName = act.activityInfo.name;

            // らくでん自身は候補に追加しない
            if (packageName.equals(RAKUDEN_PACAGE_NAME))
                continue;

            PhoneActivityData data = new PhoneActivityData(label, icon, packageName, activityName);

            // 現在選択中のアプリはチェック済みにする
            if (packageName.equals(defaultPhoneAppData.getPackageName())
                    && activityName.equals(defaultPhoneAppData.getAcitivityName())) {
                data.setSelected(true);
            }

            lists.add(data);
        }
        
        return lists;
    }
    
    private void setSummary(PhoneActivityData data){
        if(TextUtils.isEmpty(data.getApplicationName()) == false){
            this.setSummary(data.getApplicationName());
        }
    }
}
