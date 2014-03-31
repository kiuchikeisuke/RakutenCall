package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

public class SpecialNumIgnoreCondition extends AbstractCondition {

    /**
     * 特殊番号の最大長
     */
    private final int SPECIAL_NUM_MAX_LENGTH = 5;
    
    public SpecialNumIgnoreCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        return !(SharedPreferenceManager.getInstance(mContext).getSpecialNumIgnore() && isSpecialNum());
    }

    private boolean isSpecialNum() {
        return mTelNum.length() <= SPECIAL_NUM_MAX_LENGTH && mTelNum.length() > 0;
    }

}
