package jp.ne.nissing.rakutencall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactsData>{

    private LayoutInflater mLayoutInflater;
    
    public ContactsAdapter(Context context, int resource, List<ContactsData> objects) {
        super(context, resource, objects);
        
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        ContactsData item = getItem(position);
        
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.contacts_layout, null);
        }
        
        TextView displayName = (TextView) convertView.findViewById(R.id.displayName);
        displayName.setText(item.getDisplayName());
        
        TextView telNumber = (TextView) convertView.findViewById(R.id.telNumber);
        telNumber.setText(item.getTelNumber());
        
        return convertView;
    }

}
