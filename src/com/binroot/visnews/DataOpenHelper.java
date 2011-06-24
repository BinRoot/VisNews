package com.binroot.visnews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "geobase";
    private static final int DATABASE_VERSION = 2;
    private static final String COUNTRY_TABLE_CREATE =
                "CREATE TABLE " 		+ "countries" 	+ " (" +
                "CountryId" 			+ " TEXT, " 	+
                "Country" 				+ " TEXT, " 	+
                "Capital"				+ " TEXT, " 	+
                "NationalitySingular"	+ " TEXT, " 	+
                "NationalityPlural"		+ " TEXT);";

    DataOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COUNTRY_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}