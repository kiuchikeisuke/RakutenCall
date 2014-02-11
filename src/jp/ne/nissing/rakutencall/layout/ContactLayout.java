package jp.ne.nissing.rakutencall.layout;

import java.util.*;

import android.content.Context;
import android.util.AttributeSet;
import android.view.*;
import android.widget.*;

public class ContactLayout extends LinearLayout implements Checkable {
    private boolean isChecked = false;
    private List<Checkable> checkableViews;
    private OnCheckedChangeListener onCheckedChangeListener;

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(attrs);
    }

    private void initialise(AttributeSet attrs) {
        this.isChecked = false;
        this.checkableViews = new ArrayList<Checkable>(5);
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a CheckableRelativeLayout changed.
     */
    public static interface OnCheckedChangeListener {
        public void onCheckedChanged(ContactLayout layout, boolean isChecked);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;

        for (Checkable c : checkableViews) {
            c.setChecked(isChecked);
        }

        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, isChecked);
        }
    }

    @Override
    public void toggle() {
        this.isChecked = !this.isChecked;
        for (Checkable c : checkableViews) {
            c.toggle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            findCheckableChildren(this.getChildAt(i));
        }
    }

    public void setOnCheckedChangeListener(
            OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    private void findCheckableChildren(View v) {
        if (v instanceof Checkable) {
            this.checkableViews.add((Checkable) v);
        }

        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            final int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                findCheckableChildren(vg.getChildAt(i));
            }
        }
    }

}
