package jp.ne.nissing.rakutencall;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
    	DatabaseManager db = DatabaseManager.getInstance(this).open();
    	Cursor cursor = db.getContact(new ContactsData(telNum, null, null));
    	boolean isIgnoreTelNumber = cursor.moveToNext();
    	db.close();
    	
        if(telNum.startsWith(PREFIX_TELNUM) || isIgnoreTelNumber){
            return false;
        }
        return true;
    }
    
    private void callPhone(String tel){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
        startActivity(intent);
    }
}
