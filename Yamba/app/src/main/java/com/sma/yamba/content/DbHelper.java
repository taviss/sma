package com.sma.yamba.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by octavian.salcianu on 12/19/2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getName();

    public DbHelper(Context context) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String
                .format("CREATE TABLE %s (%s int PRIMARY KEY, %s text, %s text, %s int)",
                        StatusContract.TABLE,
                        StatusContract.Column.ID,
                        StatusContract.Column.USER,
                        StatusContract.Column.MESSAGE,
                        StatusContract.Column.CREATED_AT);
        Log.d(TAG, sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StatusContract.TABLE);
        onCreate(db);
    }
}
