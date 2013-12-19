package jp.ne.nissing.rakutencall;

public class DatabaseManager {
    private static DatabaseManager mInstance = null;
    
    public static DatabaseManager getInstance(){
        if(mInstance == null){
            mInstance = new DatabaseManager();
        }
        return mInstance;
    }
    
    private DatabaseManager(){
        
    }
}
