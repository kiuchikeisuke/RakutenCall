package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

public class FreeDialCondition extends AbstractCondition {

    private final String FREE_DIAL_0120 = "0120";
    private final String FREE_DIAL_0077 = "0077";
    private final String FREE_DIAL_0088 = "0088";
    
    public FreeDialCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //フリーダイアルは無視するのチェックがついている＆フリーダイアルから始まっている=>Prefixはつけない、なので真偽を反転
        return !( SharedPreferenceManager.getInstance(mContext).getFreeDialCheck() && isFreeDial());
    }

    private boolean isFreeDial() {
        return mTelNum.startsWith(FREE_DIAL_0120) || mTelNum.startsWith(FREE_DIAL_0077) || mTelNum.startsWith(FREE_DIAL_0088);
    }

}
