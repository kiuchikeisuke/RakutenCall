package jp.ne.nissing.rakutencall.preference;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import jp.ne.nissing.rakutencall.R;

public class PrefixNumberDialogEditTextPreference extends EditTextPreference {

    private Context mContext = null;
    public static final String MATCH_ASCII = "^[\\u0020-\\u007E]+$";

    public PrefixNumberDialogEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        this.setKey(SharedPreferenceManager.DEFAULT_VALUE_PREFIX_NUM);
        init();
    }

    private void init() {
        this.setSummary(SharedPreferenceManager.getInstance(mContext).getDefaultPrefixNum());
    }
    
    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        this.persistString(restoreValue ? SharedPreferenceManager.getInstance(mContext).getDefaultPrefixNum() : (String)defaultValue);
        super.onSetInitialValue(restoreValue, defaultValue);
    }

    @Override
    protected View onCreateDialogView() {
        EditText editView = this.getEditText();
        if (editView != null) {
            editView.setHint(R.string.dialog_prefix_num_hint);
            editView.setText(SharedPreferenceManager.getInstance(mContext).getDefaultPrefixNum());
            editView.setInputType(InputType.TYPE_CLASS_PHONE);
            editView.selectAll();
        }

        return super.onCreateDialogView();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String inputString = this.getEditText().getText().toString();
            // ASCII文字列範囲内かチェック。範囲外なら反映せず終了
            if (TextUtils.isEmpty(inputString) == false
                    && inputString.matches(MATCH_ASCII) == false) {
                Toast.makeText(mContext, R.string.dialog_prefix_num_error, Toast.LENGTH_LONG)
                        .show();
            } else {
                SharedPreferenceManager.getInstance(mContext).setDefaultPrefixNum(inputString);
                String prefixNum = SharedPreferenceManager.getInstance(mContext).getDefaultPrefixNum();
                Toast.makeText(
                        mContext,
                        mContext.getString(R.string.toast_prefix_num_setend, SharedPreferenceManager
                                .getInstance(mContext).getDefaultPrefixNum()), Toast.LENGTH_LONG)
                        .show();
                this.setSummary(prefixNum);
            }
        }
        super.onDialogClosed(positiveResult);
    }
}
