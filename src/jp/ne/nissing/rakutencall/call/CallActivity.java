package jp.ne.nissing.rakutencall.call;

import java.io.UnsupportedEncodingException;
import java.net.*;

import jp.ne.nissing.rakutencall.call.condition.*;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import jp.ne.nissing.rakutencall.preference.phoneappdata.PhoneActivityData;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

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
        
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));

        PhoneActivityData data = SharedPreferenceManager.getInstance(this).getDefaultPhoneApp();
        if (TextUtils.isEmpty(data.getPackageName()) == false
                && TextUtils.isEmpty(data.getAcitivityName()) == false) {
            intent.setClassName(data.getPackageName(), data.getAcitivityName());
        }

        startActivity(intent);
        finish();
    }
}
