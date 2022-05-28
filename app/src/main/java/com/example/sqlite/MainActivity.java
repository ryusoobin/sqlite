package com.example.sqlite;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main";

    Button btn_Insert;
    EditText edit_Password;
    EditText edit_Name;
    EditText edit_Email;
    EditText edit_Username;
    TextView text_Password;
    TextView text_Name;
    TextView text_Email;
    TextView text_Username;

    long nowIndex;
    String password;
    String name;
    String username;
    String email;
    String sort = "userid";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DBOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        edit_Password = (EditText) findViewById(R.id.edit_password);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Email = (EditText) findViewById(R.id.edit_email);
        edit_Username = (EditText) findViewById(R.id.edit_username);
        text_Password = (TextView) findViewById(R.id.text_password);
        text_Name = (TextView) findViewById(R.id.text_name);
        text_Email = (TextView) findViewById(R.id.text_email);
        text_Username = (TextView) findViewById(R.id.text_username);


        mDbOpenHelper = new DBOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        //showDatabase(sort);

    }

    public void setInsertMode(){
        edit_Password.setText("");
        edit_Name.setText("");
        edit_Email.setText("");
        edit_Username.setText("");
    }

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);
            edit_Name.setText(tempData[0].trim());
            edit_Password.setText(tempData[1].trim());
            edit_Email.setText(tempData[2].trim());
            edit_Username.setText(tempData[3].trim());
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            String[] nowData = arrayData.get(position).split("\\s+");
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3];
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            mDbOpenHelper.deleteColumn(nowIndex);
                            showDatabase(sort);
                            setInsertMode();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            setInsertMode();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){
            @SuppressLint("Range") String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            @SuppressLint("Range") String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            @SuppressLint("Range") String tempPassword = iCursor.getString(iCursor.getColumnIndex("password"));
            tempPassword = setTextLength(tempPassword,10);
            @SuppressLint("Range") String tempEmail = iCursor.getString(iCursor.getColumnIndex("email"));
            tempEmail = setTextLength(tempEmail,10);
            @SuppressLint("Range") String tempUsername = iCursor.getString(iCursor.getColumnIndex("username"));
            tempUsername = setTextLength(tempUsername,10);
            String Result = tempName + tempPassword + tempEmail + tempUsername;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        //arrayAdapter.clear();
        //arrayAdapter.addAll(arrayData);
        //arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                name = edit_Name.getText().toString();
                password = edit_Password.getText().toString();
                email = edit_Email.getText().toString();
                username = edit_Username.getText().toString();
                mDbOpenHelper.open();
                mDbOpenHelper.insertColumn(name, password, email, username);
                //showDatabase(sort);
                setInsertMode();
                edit_Name.requestFocus();
                edit_Name.setCursorVisible(true);
                break;
        }
    }
}