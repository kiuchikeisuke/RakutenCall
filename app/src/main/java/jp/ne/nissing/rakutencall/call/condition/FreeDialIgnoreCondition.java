package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.db.DatabaseManager;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;
import android.database.Cursor;

public class FreeDialIgnoreCondition extends AbstractCondition {
    
    private Context mContext;
    
    public FreeDialIgnoreCondition(Context context, String telNum) {
        super(context, telNum);
        mContext = context;
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //フリーダイアルは無視するのチェックがついている＆フリーダイアルから始まっている=>Prefixはつけない、なので真偽を反転
        return !( SharedPreferenceManager.getInstance(mContext).getFreeDialIgnoreCheck() && isFreeDial());
    }

    private boolean isFreeDial() {
        boolean isFreeDial = false;

        DatabaseManager db = DatabaseManager.getInstance(mContext).open();
        Cursor prefixs = db.getExceptionPrefixs();
        while(prefixs.moveToNext()){
            if( mTelNum.startsWith(prefixs.getString(prefixs.getColumnIndex(DatabaseManager.COL_TEL_NUMBER))) ){
                isFreeDial = true;
                break;
            }
        }
        
        db.close();
        return isFreeDial;
    }

}
