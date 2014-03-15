package jp.ne.nissing.rakutencall.contacts.data;

import java.util.List;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.contacts.layout.ContactLayout;
import android.content.Context;
import android.view.*;
import android.widget.*;

public class ContactsAdapter extends ArrayAdapter<ContactsData> {

    private LayoutInflater mLayoutInflater;

    public ContactsAdapter(Context context, int resource,
            List<ContactsData> objects) {
        super(context, 0, objects);

        this.mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ContactsData item = getItem(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.contacts_layout,
                    null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.displayName.setText(item.getDisplayName());
        holder.number.setText(item.getTelNumber());
        final ListView lv = (ListView) parent;
        holder.contactLayout.setChecked(lv.isItemChecked(position));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private class ViewHolder {
        TextView displayName;
        TextView number;
        ContactLayout contactLayout;

        public ViewHolder(View root) {
            displayName = (TextView) root.findViewById(R.id.displayName);
            number = (TextView) root.findViewById(R.id.telNumber);
            contactLayout = (ContactLayout) root
                    .findViewById(R.id.contacts_layout);
        }
    }
}
