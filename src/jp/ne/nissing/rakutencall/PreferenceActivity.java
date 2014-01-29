package jp.ne.nissing.rakutencall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PreferenceActivity extends android.preference.PreferenceActivity {

    private Context mContext;
    private static final String RAKUDEN_PACAGE_NAME = "jp.ne.nissing.rakutencall";
    public static final String MATCH_ASCII = "^[\\u0020-\\u007E]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_screen);
        mContext = this;
    }

    private void showSelectPhoneAppDialog() {
        PackageManager pm = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        final List<PhoneActivityData> lists = new ArrayList<PhoneActivityData>();
        PhoneActivityData defaultPhoneAppData = SharedPreferenceManager.getInstance(this)
                .getDefaultPhoneApp();
        int defaultPhoneAppIndex = 0;

        for (ResolveInfo act : activities) {
            String label = act.loadLabel(pm).toString();
            Drawable icon = act.loadIcon(pm);
            String packageName = act.activityInfo.packageName;
            String activityName = act.activityInfo.name;

            // らくでん自身は候補に追加しない
            if (packageName.equals(RAKUDEN_PACAGE_NAME))
                continue;

            PhoneActivityData data = new PhoneActivityData(label, icon, packageName, activityName);

            // 現在選択中のアプリはチェック済みにする
            if (packageName.equals(defaultPhoneAppData.getPackageName())
                    && activityName.equals(defaultPhoneAppData.getAcitivityName())) {
                defaultPhoneAppIndex = lists.size() - 1;
                data.setSelected(true);
            }

            lists.add(data);
        }

        PhoneActivityDataAdapter adapter = new PhoneActivityDataAdapter(this, 0, lists);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.phone_app_list);
        alert.setSingleChoiceItems(adapter, defaultPhoneAppIndex, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhoneActivityData phoneActivityData = lists.get(which);
                SharedPreferenceManager.getInstance(mContext).setDefaultPhoneApp(
                        phoneActivityData.getPackageName(), phoneActivityData.getAcitivityName());
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void showPrefixPhoneNumDialog() {
        final EditText editView = new EditText(this);
        editView.setHint(R.string.dialog_prefix_num_hint);
        editView.setText(SharedPreferenceManager.getInstance(this).getDefaultPrefixNum());
        editView.setInputType(InputType.TYPE_CLASS_PHONE);
        editView.selectAll();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.dialog_prefix_num_title);
        alert.setView(editView);
        alert.setPositiveButton(android.R.string.ok, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputString = editView.getText().toString();

                // ASCII文字列範囲内かチェック。範囲外なら反映せず終了
                if (TextUtils.isEmpty(inputString) == false
                        && inputString.matches(MATCH_ASCII) == false) {
                    Toast.makeText(mContext, R.string.dialog_prefix_num_error, Toast.LENGTH_LONG)
                            .show();
                    dialog.cancel();
                    dialog.dismiss();
                    return;
                }

                // SharedPrefに反映し、トーストを出して終了
                SharedPreferenceManager.getInstance(mContext).setDefaultPrefixNum(inputString);
                Toast.makeText(
                        mContext,
                        getString(R.string.toast_prefix_num_setend, SharedPreferenceManager
                                .getInstance(mContext).getDefaultPrefixNum()), Toast.LENGTH_LONG)
                        .show();
                dialog.cancel();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editView, 0);
            }
        });

        alertDialog.show();
    }

}
