package jp.ne.nissing.rakutencall.call.condition;

import android.util.Log;

public class ConditionManager {
    
    /**
     * Prefixをつけるかどうか
     * 配列内の条件をORで結合し判定<br>
     * 1つでもPrefixを付けないという判定になったらfalseを返す
     * @param conditions 条件
     * @return true->Prefixをつける false->Prefixをつけない
     */
    public static boolean isPrefixEnable(AbstractCondition[] conditions){
        boolean retval = true;
        for(AbstractCondition condition : conditions){
            //1つでもPrefixをつけないという条件があったらPrefixはつけてはならない
            if(retval == true){
                retval = condition.isPrefixEnableCondition();
                Log.d("condition", Boolean.toString(retval) +" = " + condition.getClass().toString());
            }
            else
                break;
        }
        return retval;
    }

}
