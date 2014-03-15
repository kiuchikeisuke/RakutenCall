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

}
