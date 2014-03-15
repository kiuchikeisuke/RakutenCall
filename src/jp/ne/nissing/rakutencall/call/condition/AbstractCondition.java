package jp.ne.nissing.rakutencall.call.condition;

import android.content.Context;

public abstract class AbstractCondition {
    
    protected Context mContext;
    protected String mTelNum;
    
    public AbstractCondition(final Context context,final String telNum){
        this.mContext = context;
        this.mTelNum = telNum;
    }
    
    /**
     * Prefixをつけるかどうか
     * @return true->Prefixをつける false->Prefixをつけない
     */
    public abstract boolean isPrefixEnableCondition();
}
