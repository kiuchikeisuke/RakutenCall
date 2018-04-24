package jp.ne.nissing.rakutencall.call.condition;

import jp.ne.nissing.rakutencall.contacts.data.ContactsData;
import jp.ne.nissing.rakutencall.db.DatabaseManager;
import android.content.Context;
import android.database.Cursor;

public class IgnoreTelNumCondition extends AbstractCondition {

    public IgnoreTelNumCondition(Context context, String telNum) {
        super(context, telNum);
    }

    @Override
    public boolean isPrefixEnableCondition() {
        DatabaseManager db = DatabaseManager.getInstance(mContext).open();
        Cursor cursor = db.getContact(new ContactsData(mTelNum, null, null));
        //DBに登録されている(moveToFirst=true)=>Prefixをつけない、なので真偽を反転
        boolean retval = !cursor.moveToFirst();
        db.close();
       return retval;
    }

}
