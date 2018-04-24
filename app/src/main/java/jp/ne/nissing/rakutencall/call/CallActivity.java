package jp.ne.nissing.rakutencall.call;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.call.condition.*;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import jp.ne.nissing.rakutencall.preference.phoneappdata.*;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class CallActivity extends Activity {

    private final String TEL = "tel:";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        Uri uri = intent.getData();
        if (uri == null) {
            finish();
            return;
        }

        String telNumber = uri.toString().substring(TEL.length());
        
        if (needPrefixTel(telNumber) == true) {
            callPhone(TEL + SharedPreferenceManager.getInstance(this).getDefaultPrefixNum()
                    + telNumber);
        } else {
            callPhone(TEL + telNumber);
        }
    }

    /**
     * Prefixの番号をつける必要があるかどうか
     * 判定に使う電話番号はUTF-8でデコードしたものを優先的に使用する
     * @param number
     * @return true->Prefix番号付与を行う false-> Prefix番号付与を行わない
     */
    private boolean needPrefixTel(String number) {
        String decodedTelNumber = null;
        try {
            decodedTelNumber = URLDecoder.decode(number, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String telNum = decodedTelNumber != null ? decodedTelNumber : number;
       AbstractCondition[] conditions = 
           {
               new PrefixEnableSettingCondition(this, telNum),
               new StartWithPrefixNumCondition(this, telNum),
               new IgnoreTelNumCondition(this, telNum),
               new FreeDialIgnoreCondition(this, telNum),
               new SpecialNumIgnoreCondition(this,telNum),
               new InternationalNumberCondition(this, telNum),
               };
       return ConditionManager.isPrefixEnable(conditions);
    }
    

    private void callPhone(final String tel) {

        Intent intent = getPhoneApp(tel);

        boolean checkImplicitIntent = checkImplicitIntent(this, intent);
        if(checkImplicitIntent == false){

            Toast.makeText(mContext, R.string.call_not_found_app_notice_toast, Toast.LENGTH_LONG).show();

            //起動するActivityを設定するダイアログを表示
            AlertDialog.Builder defaultAppSetDialogBuilder = new AlertDialog.Builder(mContext);
            defaultAppSetDialogBuilder.setTitle(R.string.preference_action_settings);
            int defaultPhoneAppIndex = 0;

            final List<PhoneActivityData> lists = PhoneAppUtil.getActivitis(mContext);

            for(int i = 0 ; i < lists.size(); i++){
                if(lists.get(i).isSelected()){
                    defaultPhoneAppIndex = i;
                    break;
                }
            }

            PhoneActivityDataAdapter adapter = new PhoneActivityDataAdapter(mContext,defaultPhoneAppIndex,lists);

            defaultAppSetDialogBuilder.setSingleChoiceItems(adapter, defaultPhoneAppIndex, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PhoneActivityData phoneActivityData = lists.get(which);
                    SharedPreferenceManager.getInstance(mContext).setDefaultPhoneApp(
                            phoneActivityData.getPackageName(), phoneActivityData.getAcitivityName());
                    callPhone(tel);
                }
            });

            defaultAppSetDialogBuilder.setPositiveButton(null, null);

            defaultAppSetDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(mContext,R.string.call_not_found_app_cancel_toast , Toast.LENGTH_LONG).show();
                    finish();
                }
            });

            defaultAppSetDialogBuilder.setCancelable(false);

            AlertDialog defaultAppSetDialog = defaultAppSetDialogBuilder.create();
            defaultAppSetDialog.show();

        } else {
            try{
                startActivity(intent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(this,R.string.call_not_found_app_error_toast , Toast.LENGTH_LONG).show();
            }

            finish();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    

    private Intent getPhoneApp(String tel) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));

        PhoneActivityData data = SharedPreferenceManager.getInstance(this).getDefaultPhoneApp();
        if (TextUtils.isEmpty(data.getPackageName()) == false
                && TextUtils.isEmpty(data.getAcitivityName()) == false) {
            intent.setClassName(data.getPackageName(), data.getAcitivityName());
        }
        return intent;
    }
    
    private boolean checkImplicitIntent(Context context,Intent intent){
        boolean retval = false;
        PackageManager pm = context.getPackageManager();
         List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);
        if(apps.size() > 0){
            retval = true;
        }
        return retval;
    }
}
