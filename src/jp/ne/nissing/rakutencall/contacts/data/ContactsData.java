package jp.ne.nissing.rakutencall.contacts.data;

public class ContactsData {
    private String mTelNumber;
    private String mDisplayName;
    private String mContactsId;
    private boolean isIgnored = false;

    public ContactsData(String telNumber, String displayName, String contactsId) {
        this.mTelNumber = telNumber;
        this.mDisplayName = displayName;
        this.mContactsId = contactsId;
    }
    
    /**
     * 引数に指定したContactDataと同様のデータをセットするコンストラクタ.
     * @param data
     */
    public ContactsData(ContactsData data){
        this.mTelNumber = data.getTelNumber();
        this.mDisplayName = data.getDisplayName();
        this.mContactsId = data.getContactsId();
        this.isIgnored = data.isIgnored();
    }

    public String getTelNumber() {
        return mTelNumber;
    }

    public void setTelNumber(String mTelNumber) {
        this.mTelNumber = mTelNumber;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getContactsId() {
        return mContactsId;
    }

    public void setContactsId(String mContactsId) {
        this.mContactsId = mContactsId;
    }

    public boolean isIgnored() {
        return isIgnored;
    }

    public void setIgnored(boolean isIgnored) {
        this.isIgnored = isIgnored;
    }
    
    @Override
    public String toString() {
        return getTelNumber()+getDisplayName();
    }
}
