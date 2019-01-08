package com.example.app.sampletest.CommonUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.app.sampletest.ModelViewPresenter.Model.VideoListPojo;
import java.util.ArrayList;
import java.util.List;

import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_BACK_POSITION;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_CURRENT_WINDOW;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_DESCRIPTION;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_ID;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_THUMB;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_TITLE;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_URL;
import static com.example.app.sampletest.CommonUtils.Constants.COLUMN_VIDEO_ID;
import static com.example.app.sampletest.CommonUtils.Constants.DATABASE_NAME;
import static com.example.app.sampletest.CommonUtils.Constants.DATABASE_VERSION;
import static com.example.app.sampletest.CommonUtils.Constants.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_VIDEO_ID + " INTEGER,"
                    +COLUMN_THUMB + " TEXT,"
                    +COLUMN_TITLE + " TEXT,"
                    +COLUMN_URL + " TEXT,"
                    +COLUMN_CURRENT_WINDOW + " INTEGER,"
                    +COLUMN_BACK_POSITION + " LONG"
                    + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public long insertVideo(VideoListPojo videoListPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_DESCRIPTION, videoListPojo.getDescription());
        values.put(COLUMN_VIDEO_ID,videoListPojo.getId());
        values.put(COLUMN_THUMB,videoListPojo.getThumb());
        values.put(COLUMN_TITLE,videoListPojo.getTitle());
        values.put(COLUMN_URL,videoListPojo.getUrl());
        values.put(COLUMN_CURRENT_WINDOW,videoListPojo.getCurrentWindow());
        values.put(COLUMN_BACK_POSITION,videoListPojo.getPlayBackPosition());

        long id = db.insert(TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public VideoListPojo getVideo(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_DESCRIPTION,COLUMN_VIDEO_ID,COLUMN_THUMB,COLUMN_TITLE,COLUMN_URL,COLUMN_CURRENT_WINDOW,COLUMN_BACK_POSITION},
                COLUMN_VIDEO_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        VideoListPojo note = new VideoListPojo(
                cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_THUMB)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_URL)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_CURRENT_WINDOW)),
                cursor.getLong(cursor.getColumnIndex(COLUMN_BACK_POSITION))
                );

        cursor.close();
        return note;
    }

    public List<VideoListPojo> getAllVideo() {
        List<VideoListPojo> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                VideoListPojo videoListPojo = new VideoListPojo();
                videoListPojo.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                videoListPojo.setId(cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)));
                videoListPojo.setThumb(cursor.getString(cursor.getColumnIndex(COLUMN_THUMB)));
                videoListPojo.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                videoListPojo.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                videoListPojo.setCurrentWindow(cursor.getInt(cursor.getColumnIndex(COLUMN_CURRENT_WINDOW)));
                videoListPojo.setPlayBackPosition(cursor.getLong(cursor.getColumnIndex(COLUMN_BACK_POSITION)));
                notes.add(videoListPojo);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;
    }

    public int getVideoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateVideo(VideoListPojo videoListPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CURRENT_WINDOW, videoListPojo.getCurrentWindow());
        values.put(COLUMN_BACK_POSITION,videoListPojo.getPlayBackPosition());
        return db.update(TABLE_NAME, values, COLUMN_VIDEO_ID + " = ?",
                new String[]{String.valueOf(videoListPojo.getId())});
    }

    public void deleteVideo(VideoListPojo videoListPojo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_VIDEO_ID + " = ?",
                new String[]{String.valueOf(videoListPojo.getId())});
        db.close();
    }
    public void deleteALlVideosFromDb(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME); //delete all rows in a table
        db.close();
    }
}