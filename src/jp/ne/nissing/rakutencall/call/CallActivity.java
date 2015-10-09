package jp.ne.nissing.rakutencall.call;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;

import jp.ne.nissing.rakutencall.call.condition.*;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import jp.ne.nissing.rakutencall.preference.phoneappdata.PhoneActivityData;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class CallActivity extends Activity {

    private final String TEL = "tel:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    

    private void callPhone(String tel) {
        
        Intent intent = getPhoneApp(tel);
        
        boolean checkImplicitIntent = checkImplicitIntent(this, intent);
        if(checkImplicitIntent == false){
            SharedPreferenceManager.getInstance(this).resetDefaultPhoneApp();
            PhoneActivityData data = SharedPreferenceManager.getInstance(this).getDefaultPhoneApp();
            Toast.makeText(this, "正しくアプリを認識できませんでした。「"+data.getApplicationName()+"」アプリを起動します", Toast.LENGTH_LONG).show();
            intent = getPhoneApp(tel);
        }
        try{
            startActivity(intent);
        }catch(ActivityNotFoundException e){
            SharedPreferenceManager.getInstance(this).resetDefaultPhoneApp();
            PhoneActivityData data = SharedPreferenceManager.getInstance(this).getDefaultPhoneApp();
            Toast.makeText(this, "正しくアプリを認識できませんでした。「"+data.getApplicationName()+"」アプリを起動します", Toast.LENGTH_LONG).show();
            intent = getPhoneApp(tel);
            startActivity(intent);
        }
        
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
