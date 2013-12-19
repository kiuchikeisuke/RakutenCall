package jp.ne.nissing.rakutencall;

public class ContactsData {
    private String mTelNumber;
    private String mDisplayName;
    private String mContactsId;
    
    public ContactsData(String telNumber, String displayName, String contactsId){
        this.mTelNumber = telNumber;
        this.mDisplayName = displayName;
        this.mContactsId = contactsId;
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
    
    
}
