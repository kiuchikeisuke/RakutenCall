package jp.ne.nissing.rakutencall.contacts.data;

import java.util.ArrayList;
import java.util.List;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.contacts.ContactsManager;
import jp.ne.nissing.rakutencall.contacts.layout.ContactLayout;

import android.content.Context;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;

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
            List<ContactsData> ignoreContactList = ContactsManager.getInstance(getContext()).getIgnoreContactListClone(false);
            List<ContactsData> clone = new ArrayList<ContactsData>();
            for(ContactsData data : ignoreContactList){
                clone.add(new ContactsData(data));
            }
            if(TextUtils.isEmpty(constraint) == false){
                String[] keywords = constraint.toString().split("\\s");
                List<ContactsData> filteredItems = new ArrayList<ContactsData>();
                for(ContactsData item : clone){
                    if(isMatched(item,keywords)){
                        filteredItems.add(item);
                    }
                }
                results.count = filteredItems.size();
                results.values = filteredItems;
            } else {
                synchronized (this) {
                    results.count = clone.size();
                    results.values = clone;
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
            for(ContactsData item : items){
                //FIXME:ここで追加されるオブジェクトがContactsManagerで管理されているものか、クローンなのかによって動作が変わってしまっている。
                //全体的な動作を踏まえたうえでどうするか考察する必要あり->レポジトリの構造かなぁーというかんじ
                add(item); 
            }
            notifyDataSetInvalidated();
        }
        
    }
}
