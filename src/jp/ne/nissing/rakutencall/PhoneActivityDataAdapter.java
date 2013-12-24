package jp.ne.nissing.rakutencall;

import java.util.List;

import android.content.Context;
import android.view.*;
import android.widget.*;

public class PhoneActivityDataAdapter extends ArrayAdapter<PhoneActivityData>{

    private LayoutInflater mLayoutInflater;

    public PhoneActivityDataAdapter(Context context, int resource, List<PhoneActivityData> objects) {
    	super(context, resource, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PhoneActivityData item = getItem(position);

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.phone_app_layout, null);
        }
        
        ImageView imageView = (ImageView) convertView.findViewById(R.id.appIcon);
        imageView.setImageDrawable(item.getIcon());
        
        TextView textView = (TextView) convertView.findViewById(R.id.appName);
        textView.setText(item.getApplicationName());
        
        RadioButton btn = (RadioButton) convertView.findViewById(R.id.appRadioButton);
        btn.setChecked(item.isSelected());
        
        return convertView;
    }

}
