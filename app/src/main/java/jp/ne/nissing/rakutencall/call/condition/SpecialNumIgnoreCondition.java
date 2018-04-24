package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

/**
 * 特殊番号スタートを無視するかどうか
 *
 */
public class SpecialNumIgnoreCondition extends AbstractCondition {

    /**
     * 特殊番号の最大長
     */
    private final int SPECIAL_NUM_MAX_LENGTH = 5;
    private static final String SPECIAL_CHAR_SHARP = "#";
    private static final String SPECIAL_CHAR_ASTARISK = "*";
    
    public SpecialNumIgnoreCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        return !(SharedPreferenceManager.getInstance(mContext).getSpecialNumIgnore() && isSpecialNum());
    }

    private boolean isSpecialNum() {
        boolean isOverNumLength = mTelNum.length() <= SPECIAL_NUM_MAX_LENGTH && mTelNum.length() > 0;
        boolean isStartWithSpecialChar = mTelNum.startsWith(SPECIAL_CHAR_SHARP) || mTelNum.startsWith(SPECIAL_CHAR_ASTARISK);
        return isOverNumLength || isStartWithSpecialChar;
    }

}
