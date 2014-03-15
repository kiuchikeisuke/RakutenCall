package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

public class PrefixEnableSettingCondition extends AbstractCondition {

    public PrefixEnableSettingCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //Prefix設定が有効=>Prefixをつける
        return SharedPreferenceManager.getInstance(mContext).getPrefixEnable();
    }

}
