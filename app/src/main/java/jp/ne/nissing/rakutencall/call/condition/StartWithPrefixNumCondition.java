package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;

public class StartWithPrefixNumCondition extends AbstractCondition {

    public StartWithPrefixNumCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //Prefixの番号が既に付与されている(true)=>再度つけない、なので真偽を反転
        return !mTelNum.startsWith(SharedPreferenceManager.getInstance(mContext).getDefaultPrefixNum());
    }

}
