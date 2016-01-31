package jp.ne.nissing.rakutencall.preference.exception_prefix;

import java.util.*;

import jp.ne.nissing.rakutencall.R;
import jp.ne.nissing.rakutencall.db.DatabaseManager;
import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ExceptionPrefixActivity extends Activity{
    private Context mContext;
    private ExceptionPrefixAdapter mExceptionPrefixAdapter = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exception_prefix_activity);
        mContext = this;
        
        init();
    }
    
    private void init(){
        final ListView exceptionPrefixListView = (ListView) findViewById(R.id.exceptionPrefixList);

        //対象外プレフィックスのリストを生成
        DatabaseManager.getInstance(mContext).open();
        Cursor cursor = DatabaseManager.getInstance(mContext).getExceptionPrefixs();
        final List<String> exceptionPrefixList = new ArrayList<String>();
        
        while(cursor.moveToNext()){
            exceptionPrefixList.add(cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_TEL_NUMBER)));
        }
        DatabaseManager.getInstance(mContext).close();
        
        
        //Adapter設定
        mExceptionPrefixAdapter = new ExceptionPrefixAdapter(mContext, 0, exceptionPrefixList);
        exceptionPrefixListView.setAdapter(mExceptionPrefixAdapter);
        
        
        //削除動作設定
        exceptionPrefixListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                final String prefixNumber = exceptionPrefixList.get(position);
                
                AlertDialog.Builder deleteCheckDialogBuilder = new AlertDialog.Builder(mContext);
                deleteCheckDialogBuilder.setTitle(R.string.exception_prefix_dialog_title);
                deleteCheckDialogBuilder.setMessage(prefixNumber + "を削除しますか?");
                deleteCheckDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mExceptionPrefixAdapter.remove(prefixNumber);
                        DatabaseManager.getInstance(mContext).open();
                        DatabaseManager.getInstance(mContext).deleteTargetExceptionPrefix(prefixNumber);
                        DatabaseManager.getInstance(mContext).close();
                    }
                });
                
                deleteCheckDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                    
                });
                
                deleteCheckDialogBuilder.setCancelable(true);
                AlertDialog deleteCheckDialog = deleteCheckDialogBuilder.create();
                deleteCheckDialog.show();
            }
        });
        
        
        //追加ボタン動作設定
        Button addButton = (Button) findViewById(R.id.addExceptionPrefixButton);
        addButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final EditText inputText = new EditText(mContext);
                inputText.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog.Builder addDialogBuilder = new AlertDialog.Builder(mContext);
                addDialogBuilder.setView(inputText);
                addDialogBuilder.setTitle(R.string.exception_prefix_add_dialog_title);
                
                addDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String prefixNumber = inputText.getText().toString();
                        Toast.makeText(mContext, prefixNumber + "を対象外プレフィックスに追加しました", Toast.LENGTH_LONG).show();
                        mExceptionPrefixAdapter.add(prefixNumber);
                        DatabaseManager.getInstance(mContext).open();
                        DatabaseManager.getInstance(mContext).updateTargetExceptionPrefix(prefixNumber);
                        DatabaseManager.getInstance(mContext).close();
                        
                    }
                });
                addDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                
                AlertDialog addDialog = addDialogBuilder.create();
                addDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    
                    @Override
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        im.showSoftInput(inputText, 0);
                    }
                });
                addDialog.show();
                
            }
        });
    }
}
