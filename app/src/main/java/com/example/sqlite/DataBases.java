package com.example.sqlite;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns{
        public static final String PASSWORD = "password";
        public static final String NAME = "name";
        public static final String USERNAME = "username";
        public static final String EMAIL = "email";
        public static final String _TABLENAME0 = "usertable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +NAME+" text not null , "
                +PASSWORD+" text not null , "
                +EMAIL+" text not null, "
                +USERNAME+" text not null );";
    }
}