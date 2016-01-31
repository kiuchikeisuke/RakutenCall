package jp.ne.nissing.rakutencall.preference.phoneappdata;

import java.util.*;

import jp.ne.nissing.rakutencall.preference.SharedPreferenceManager;
import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class PhoneAppUtil {

    private static final String RAKUDEN_PACAGE_NAME = "jp.ne.nissing.rakutencall";
    
    public static  List<PhoneActivityData> getActivitis(Context context){
        List<PhoneActivityData> lists = new ArrayList<PhoneActivityData>();

        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        PhoneActivityData defaultPhoneAppData = SharedPreferenceManager.getInstance(context)
                .getDefaultPhoneApp();

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
                data.setSelected(true);
            }

            lists.add(data);
        }
        
        return lists;
    }
}
