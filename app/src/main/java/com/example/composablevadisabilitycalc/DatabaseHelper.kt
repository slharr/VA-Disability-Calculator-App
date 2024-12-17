package com.example.composablevadisabilitycalc

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dee_lite_studios.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USER = "User"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        const val TABLE_DISABILITY = "Disability"
        const val COLUMN_DISABILITY_ID = "disability_id"
        const val COLUMN_USER_ID_FK = "user_id"
        const val COLUMN_DISABILITY = "disability"
        const val COLUMN_PERCENTAGE = "percentage"
    } // End of companion object creation of database properties

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery = ("CREATE TABLE $TABLE_USER (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT UNIQUE NOT NULL, " +
                "$COLUMN_PASSWORD TEXT NOT NULL)")

        val createDisabilityTableQuery = ("CREATE TABLE $TABLE_DISABILITY (" +
                "$COLUMN_DISABILITY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_ID_FK INTEGER, " +
                "$COLUMN_DISABILITY TEXT, " +
                "$COLUMN_PERCENTAGE DOUBLE, " +
                "FOREIGN KEY ($COLUMN_USER_ID_FK) REFERENCES $TABLE_USER($COLUMN_USER_ID))")
        // Foreign key linking to the user's ID (creates a relationship between the User and Disability tables).

        db?.execSQL(createUserTableQuery)
        db?.execSQL(createDisabilityTableQuery)
    } // End of onCreate function

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
    }

    fun insertUser(username: String, password: String): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USER, null, contentValues)
    } // end of insertUser function

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "$COLUMN_USERNAME= ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)

        val cursor = db.query(
            TABLE_USER,  // The table to query
            null,     // The columns to return
            selection, // The columns for the WHERE clause
            selectionArgs, // The values for the WHERE clause
            null,     // Don't group the rows
            null,     // Don't filter by row groups
            null      // The sort order
        )

        val cursorCount = cursor.count
        cursor.close()
        db.close()

        return cursorCount > 0
    } // End of checkUser function

    fun getUserId(username: String): Long? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
        var userId: Long? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID))
        }
        cursor.close()
        db.close()
        return userId
    } // End of getUserId function




    fun insertDisability(userId: Long, disability: String, percentage: Double): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_ID_FK, userId)
            put(COLUMN_DISABILITY, disability)
            put(COLUMN_PERCENTAGE, percentage)
        }
        return db.insert(TABLE_DISABILITY, null, contentValues)
    }

    fun updateDisability(disabilityId: Long, disability: String, percentage: Double) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_DISABILITY, disability)
            put(COLUMN_PERCENTAGE, percentage)
        }
        db.update(TABLE_DISABILITY, contentValues, "$COLUMN_DISABILITY_ID = ?", arrayOf(disabilityId.toString()))
    }

    fun getDisabilities(userId: Long):List<Triple<Long, String, Double>> {
        val db = readableDatabase
        val cursor = db.query(TABLE_DISABILITY, null, "$COLUMN_USER_ID_FK = ?", arrayOf(userId.toString()), null, null, null)
        val disabilities = mutableListOf<Triple<Long, String, Double>>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DISABILITY_ID))
            val disability = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISABILITY))
            val percentage = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PERCENTAGE))
            disabilities.add(Triple(id, disability, percentage))
        }
        cursor.close()
        return disabilities
    }


//    fun deleteAllDisabilities() {
//        val db = this.writableDatabase
//        db.delete(TABLE_DISABILITY, null, null)
//    }

    fun deleteAllDisabilitiesForUser(userId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_DISABILITY, "$COLUMN_USER_ID_FK=?", arrayOf(userId.toString()))
        //db.execSQL("DELETE FROM TABLE_DISABILITY") // Delete all rows
        //db.execSQL("VACUUM") // Reset the auto-increment counter
    }
    // Function to delete empty values and reset IDs
    fun cleanAndResetDisabilityTable(db: SQLiteDatabase) {
        // Delete rows where COLUMN_DISABILITY or COLUMN_PERCENTAGE is empty or null
        db.execSQL("DELETE FROM $TABLE_DISABILITY WHERE $COLUMN_DISABILITY IS NULL OR $COLUMN_DISABILITY = '' OR $COLUMN_PERCENTAGE IS NULL")

        // Reset the ID sequence
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='$TABLE_DISABILITY'")
        db.execSQL("VACUUM")
    }



} // End of class
