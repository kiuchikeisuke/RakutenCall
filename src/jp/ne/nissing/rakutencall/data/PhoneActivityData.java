package jp.ne.nissing.rakutencall.data;

import android.graphics.drawable.Drawable;

public class PhoneActivityData {
    private String mApplicationName;
    private Drawable mIcon;
    private String mPackageName;
    private String mAcitivityName;
    private boolean isSelected = false;

    public PhoneActivityData(String applicationName, Drawable icon, String packageName, String activityName){
        this.mApplicationName = applicationName;
        this.mIcon = icon;
        this.mPackageName = packageName;
        this.mAcitivityName = activityName;
    }

    public String getApplicationName() {
        return mApplicationName;
    }
    public void setApplicationName(String ApplicationName) {
        this.mApplicationName = ApplicationName;
    }
    public Drawable getIcon() {
        return mIcon;
    }
    public void setIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }
    public String getPackageName() {
        return mPackageName;
    }
    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getAcitivityName() {
        return mAcitivityName;
    }

    public void setAcitivityName(String mAcitivityName) {
        this.mAcitivityName = mAcitivityName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
