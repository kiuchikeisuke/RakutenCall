package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

public class FreeDialCondition extends AbstractCondition {

    private final String FREE_DIAL = "0120";
    
    public FreeDialCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //フリーダイアルは無視するのチェックがついている＆フリーダイアルから始まっている=>Prefixはつけない、なので真偽を反転
        return !( SharedPreferenceManager.getInstance(mContext).getFreeDialCheck() && mTelNum.startsWith(FREE_DIAL));
    }

}
