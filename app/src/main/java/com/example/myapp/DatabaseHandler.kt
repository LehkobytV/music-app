package com.example.myapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.util.Log

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "instrumentsManager"
        private val TABLE_INSTRUMENTS = "instruments"
        private val KEY_ID = "id"
        private val KEY_TYPE = "type"
        private val KEY_BRAND = "brand"
        private val KEY_MODEL = "model"
        private val KEY_COUNTRY = "country"
        private val KEY_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createInstrumentsTable = ("CREATE TABLE $TABLE_INSTRUMENTS (" +
                "$KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_TYPE TEXT," +
                "$KEY_BRAND TEXT," +
                "$KEY_MODEL TEXT," +
                "$KEY_COUNTRY TEXT," +
                "$KEY_PRICE REAL)")
        db.execSQL(createInstrumentsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INSTRUMENTS")
        onCreate(db)
    }

    fun addInstrument(instrument: Instruments) {
        val db = this.writableDatabase

        try {
            val values = ContentValues().apply {
                put(KEY_TYPE, instrument.type)
                put(KEY_BRAND, instrument.brand)
                put(KEY_MODEL, instrument.model)
                put(KEY_COUNTRY, instrument.countryOfOrigin)
                put(KEY_PRICE, instrument.price)
            }

            db.insert(TABLE_INSTRUMENTS, null, values)
        } catch (e: Exception) {
            Log.e("DatabaseHandler", "Error while adding instrument", e)
        } finally {
            db.close()
        }
    }

    fun getAllInstruments(): List<Instruments> {
        val instrumentsList = ArrayList<Instruments>()
        val selectQuery = "SELECT  * FROM $TABLE_INSTRUMENTS"

        val db = this.readableDatabase
        try {
            db.rawQuery(selectQuery, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val typeId = cursor.getColumnIndex(KEY_TYPE)
                        val brandId = cursor.getColumnIndex(KEY_BRAND)
                        val modelId = cursor.getColumnIndex(KEY_MODEL)
                        val countryId = cursor.getColumnIndex(KEY_COUNTRY)
                        val priceId = cursor.getColumnIndex(KEY_PRICE)
                        val idId = cursor.getColumnIndex(KEY_ID)

                        if (typeId != -1 && brandId != -1 && modelId != -1 && countryId != -1 && priceId != -1 && idId != -1) {
                            val instrument = Instruments(
                                type = cursor.getString(typeId),
                                brand = cursor.getString(brandId),
                                model = cursor.getString(modelId),
                                countryOfOrigin = cursor.getString(countryId),
                                price = cursor.getDouble(priceId)
                            ).apply {
                                id = cursor.getInt(idId)
                            }
                            instrumentsList.add(instrument)
                        }
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            Log.e("DatabaseHandler", "Error while fetching instruments", e)
        } finally {
            db.close()
        }

        return instrumentsList
    }


}