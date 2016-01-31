package jp.ne.nissing.rakutencall.preference.exception_prefix;

import java.util.List;

import jp.ne.nissing.rakutencall.R;
import android.content.Context;
import android.view.*;
import android.widget.*;

public class ExceptionPrefixAdapter extends ArrayAdapter<String>{

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    
    public ExceptionPrefixAdapter(Context context, int resource,
            List<String> objects) {
        super(context, 0, objects);
        mContext = context;
        this.mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        
        final String prefixNumber = getItem(position);
        
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.exception_prefix_layout, null);
        }
        
        TextView prefixText = (TextView) convertView.findViewById(R.id.prefixNumber);
        prefixText.setText(prefixNumber);
        
        return convertView;
    }

}
