package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.Context;
import android.content.res.Resources;

/**
 * 国際番号に対する動作
 *
 */
public class InternationalNumberCondition extends AbstractCondition {

    private static final String IDENTIFICATION_NUM = "010";
    private static final String IDENTIFICATION_NUM_KDDI = "001010";
    private static final String IDENTIFICATION_PLUS = "+";
    
    public InternationalNumberCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        //国際電話の識別子から始まらない番号は判定の対象外とする
        if(mTelNum.startsWith(IDENTIFICATION_NUM) == false && mTelNum.startsWith(IDENTIFICATION_PLUS) == false && mTelNum.startsWith(IDENTIFICATION_NUM_KDDI) == false){
            return true;
        }
        //国際番号の識別子から開始だが,設定でOFFの場合は常にPrefixを付けない
        if(SharedPreferenceManager.getInstance(mContext).getInternationalNum() == false){
            return false;
        }
        
        boolean retval = false;
        
        String telNum = removeIdentificationNum(mTelNum);
        
        Resources res = mContext.getResources();
        String[] enableNumbers = res.getStringArray(R.array.enable_numbers);
        String[] disableNumbers = res.getStringArray(R.array.disable_numbers);
        
        //Prefixに対応している番号かをチェック
        for(String enableNum : enableNumbers){
            if(telNum.startsWith(enableNum)){
                retval = true;
                break;
            }
        }
        //Prefixに対応していない番号かをチェック
        for(String disableNum : disableNumbers){
            if(telNum.startsWith(disableNum)){
                retval = false;
                break;
            }
        }
        return retval;
    }
    
    private String removeIdentificationNum(String telNum){
        String retval = telNum;
        if(telNum.startsWith(IDENTIFICATION_NUM)){
            retval = telNum.substring(IDENTIFICATION_NUM.length());
        }else if(telNum.startsWith(IDENTIFICATION_NUM_KDDI)){
            retval = telNum.substring(IDENTIFICATION_NUM_KDDI.length());
        }else if(telNum.startsWith(IDENTIFICATION_PLUS)){
            retval = telNum.substring(IDENTIFICATION_PLUS.length());
        }
        return retval;
    }
}
