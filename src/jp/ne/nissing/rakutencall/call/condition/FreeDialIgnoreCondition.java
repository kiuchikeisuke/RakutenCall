package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

public class FreeDialIgnoreCondition extends AbstractCondition {

    private final String FREE_DIAL_0120 = "0120";
    private final String FREE_DIAL_0077 = "0077";
    private final String FREE_DIAL_0088 = "0088";
    
    public FreeDialIgnoreCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //フリーダイアルは無視するのチェックがついている＆フリーダイアルから始まっている=>Prefixはつけない、なので真偽を反転
        return !( SharedPreferenceManager.getInstance(mContext).getFreeDialIgnoreCheck() && isFreeDial());
    }

    private boolean isFreeDial() {
        return mTelNum.startsWith(FREE_DIAL_0120) || mTelNum.startsWith(FREE_DIAL_0077) || mTelNum.startsWith(FREE_DIAL_0088);
    }

}
