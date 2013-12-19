package jp.ne.nissing.rakutencall;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class CallActivity extends Activity{

    private final String TEL = "tel:";
    private final String PREFIX_TELNUM = "003768";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        if(intent == null){
            finish();
            return;
        }
        
        Uri uri = intent.getData();
        if(uri == null){
            finish();
            return;
        }
        
        String telNumber = uri.toString().substring(TEL.length());
        
        if(needPrefixTel(telNumber) == true){
            callPhone(TEL + PREFIX_TELNUM + telNumber);
        }
        else{
            callPhone(TEL + telNumber);
        }
    }
    
    private boolean needPrefixTel(String telNum){
        if(telNum.startsWith(PREFIX_TELNUM) /*ここに無視リストに含まれているかどうかをチェックする*/){
            return false;
        }
        return true;
    }
    
    private void callPhone(String tel){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
        startActivity(intent);
    }
}
