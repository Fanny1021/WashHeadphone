package com.fanny.washhead.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fanny on 17/6/13.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBOpenHelper(Context context) {
        super(context, "WashHeadData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 用户表
        db.execSQL("create table IF NOT EXISTS user(name varchar(50),sex varchar(10),idcardno varchar(50))");
        // 体温表
        db.execSQL("create table IF NOT EXISTS tempData(name varchar(50),time text,temp float)");
        // 血压表
        db.execSQL("create table IF NOT EXISTS xueyaData(name varchar(50),time text,sys integer,dia integer,plus integer,map integer)");
        // 血糖表
        db.execSQL("create table IF NOT EXISTS xuetangData(name varchar(50),time text,glu float,ua float,chol float)");
        // 心电表
        db.execSQL("create table IF NOT EXISTS xindianData(name varchar(50),time text,ecg integer)");
        // 血氧表
        db.execSQL("create table IF NOT EXISTS xueyangData(name varchar(50),time text,spo2 integer,pr integer)");
        // 尿酸表

        // 手环表
        db.execSQL("create table IF NOT EXISTS shouhuanData(name varchar(50),time text,spo2 integer,pr integer,pi float)");

        // 测试用户登录信息表
        db.execSQL("create table IF NOT EXISTS userlogin(name varchar(50),sex varchar(10),age varchar(50),idcardno varchar(50))");


        db.execSQL("create table IF NOT EXISTS hoster(account varchar(50),psw varchar(50))");

//        db.execSQL("create table IF NOT EXISTS data(name varchar(50),time date,sys integer,dia integer,pul integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists tempData");
        db.execSQL("drop table if exists xueyaData");
        db.execSQL("drop table if exists xuetangData");
        db.execSQL("drop table if exists xindianData");
        db.execSQL("drop table if exists xueyangData");
        db.execSQL("drop table if exists shouhuanData");
        db.execSQL("drop table if exists userlogin");
        db.execSQL("drop table if exists hoster");
        onCreate(db);
    }
}
