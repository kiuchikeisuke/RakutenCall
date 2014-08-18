package jp.ne.nissing.rakutencall.contacts.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.contacts.ContactsRepository;
import jp.ne.nissing.rakutencall.contacts.layout.ContactLayout;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<ContactsData> {

    private LayoutInflater mLayoutInflater;
    private ContactsFilter mFilter;
    
    
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
        lv.setItemChecked(position, item.isIgnored());
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
    
    @Override
    public Filter getFilter() {
        if(mFilter == null){
            mFilter = new ContactsFilter();
        }
        return mFilter;
    }
    
    private class ContactsFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ContactsData> ignoreContactList = ContactsRepository.getInstance(getContext()).getIgnoreContactListClone();
            if(TextUtils.isEmpty(constraint) == false){
                String[] keywords = constraint.toString().split("\\s");
                List<ContactsData> filteredItems = new ArrayList<ContactsData>();
                for(ContactsData item : ignoreContactList){
                    if(isMatched(item,keywords)){
                        filteredItems.add(item);
                    }
                }
                synchronized (this) {
                    results.count = filteredItems.size();
                    results.values = filteredItems;
                }
            } else {
                synchronized (this) {
                    results.count = ignoreContactList.size();
                    results.values = ignoreContactList;
                }
            }
            return results;
        }

        private boolean isMatched(ContactsData item, String[] keywords) {
            boolean retval = true;
            for(String keyword : keywords){
                if(item.toString().contains(keyword) == false)
                    retval = false;
            }
            return retval;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ContactsData> items = (List<ContactsData>) results.values;
            
            notifyDataSetChanged();
            clear();
            addAll(items);
            notifyDataSetInvalidated();
        }
        
    }
}
