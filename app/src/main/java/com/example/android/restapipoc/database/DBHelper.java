package com.example.android.restapipoc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.restapipoc.model.CustomerResponse;
import com.example.android.restapipoc.model.OrderResponce;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DBHelper extends OrmLiteSqliteOpenHelper {

    // Fields

    public static final String DB_NAME = "student_manager.db";
    private static final int DB_VERSION = 1;

    // Public methods

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource cs) {
        try {
            // Create Table with given table name with columnName
            TableUtils.createTable(cs, CustomerResponse.class);
            TableUtils.createTable(cs, OrderResponce.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int oldVersion, int newVersion) {

    }


    public <T> List<T> getAll(Class<T> clazz) {
        Dao<T, ?> dao = null;
        try {
            dao = getDao(clazz);
            return dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public <T> List<T> getAllOrdered(Class<T> clazz, String orderBy, boolean ascending) throws SQLException {
        try {
            Dao<T, ?> dao = getDao(clazz);
            return dao.queryBuilder().orderBy(orderBy, ascending).query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public <T> void fillObject(Class<T> clazz, T aObj) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            dao.createOrUpdate(aObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void fillObjects(Class<T> clazz, Collection<T> aObjList) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            for (T obj : aObjList) {
                dao.createOrUpdate(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> query(Class<T> clazz, Map<String, Object> aMap) {
        Dao<T, ?> dao = null;
        try {
            dao = getDao(clazz);
            return dao.queryForFieldValues(aMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

}