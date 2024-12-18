package com.example.vadisabilitycalc

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context):
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "VACalcDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Users"
        internal const val TABLE_DISABILITY = "Disabilities"
        private const val  COLUMN_USER_ID = "ID"
        private const val  COLUMN_USERNAME = "username"
        private const val  COLUMN_PASSWORD = "password"
        private const val COLUMN_DISABILITY_ID = "disID"
        internal const val COLUMN_DISABILITY = "disability"
        internal const val COLUMN_PERCENTAGE = "percentage"
        private const val USER_ID = "key ID"
        // this creates constant variables for each value for a database creation
        // SQLite Database Tables CANNOT be deleted through programming!
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "$COLUMN_USERNAME TEXT UNIQUE NOT NULL, " +
                "$COLUMN_PASSWORD TEXT NOT NULL)")
        val createDisabiltyQuery = ("CREATE TABLE $TABLE_DISABILITY (" +
                "$COLUMN_DISABILITY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$USER_ID INTEGER, " +
                "$COLUMN_DISABILITY TEXT, " +
                "$COLUMN_PERCENTAGE DOUBLE, " +
                "FOREIGN KEY (USER_ID) REFERENCES ($COLUMN_USER_ID)")
        db?.execSQL(createTableQuery)
        db?.execSQL(createDisabiltyQuery)
        // This function creates a database where one does not exist. It creates the tables.
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = ("DROP TABLE IF EXISTS $TABLE_NAME")
        val dropDisabiltyQuery = ("DROP TABLE IF EXISTS $TABLE_DISABILITY")
        db?.execSQL(dropTableQuery)
        db?.execSQL(dropDisabiltyQuery)
        onCreate(db)
    }// function to update database

    fun insertUser(username: String, password: String): Long{
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
        // Inserts the values of name and password into the table
    }

    fun lookForUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
        // If the user name and password are present within the database this will return true
        // The arguments will create a negative integer if the database has no matches and positive if there are
    }

    fun deleteSingleRow(id: Int) {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME WHERE $COLUMN_USER_ID=$id")
        db.close()
        println("DONE")
    } // TEST to remove row from User List

    // NEXT BLOCK IS FOR DISABILITY LISTS

    fun insertDisabilities(disability: String, percentage: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_DISABILITY, disability)
            put(COLUMN_PERCENTAGE, percentage)
        }
        return db.insert(TABLE_DISABILITY, null, values)
        // Inserts the values of disability and percentage into the table
    }

    fun updateTheDisabilityData(username: String, disability: String, percentage: Double) :Int{
        val dataBase = writableDatabase
        val newValues = ContentValues()
        newValues.put(COLUMN_DISABILITY, disability)
        newValues.put(COLUMN_PERCENTAGE, percentage)
        return dataBase.update(TABLE_DISABILITY, newValues, "$COLUMN_DISABILITY_ID=?", arrayOf(username.toString()))
    } // This function will edit the items if user needs to do so
    // Normally a database would use the id as the first reference. In this case the username is being used.

    fun getAllDataFromDisabilities(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_DISABILITY", null)
    }

    private fun deleteData(disID: Int): Int{
        val dataBase = writableDatabase
        return dataBase.delete(TABLE_DISABILITY, "$COLUMN_DISABILITY_ID=?", arrayOf(disID.toString()))
    } // This function will delete all the data within a specific users work area.

    //  ****  TEST ****
    // BELOW THIS BLOCK ARE SEVERAL FUNCTIONS TO DELETE A DATABASE OR ITEMS FROM A DATABASE

    fun deleteDatabase(calculatorActivity: CalculatorActivity): Boolean {
        return context.deleteDatabase(DATABASE_NAME)
    }// To delete entire database and using Boolean for if true statement *ATTEMPT*

    fun deleteAllFromDatabase(){
        val oldData = this.writableDatabase
        oldData.delete(DATABASE_NAME, null, null)
    } // To delete entire database *ATTEMPT*

    fun deleteDatabaseItems(id: String) {
        val oldData = this.getWritableDatabase()
        return oldData.execSQL("DELETE FROM VACalcUsers.db")
        // This will delete all data from within that database *ATTEMPT*
    }

    fun deleteDatabaseSingleRow(id: Int): Int {
        val oldData = this.getWritableDatabase()
        return oldData.delete(TABLE_NAME, "$COLUMN_USER_ID=?", arrayOf(id.toString()))
    }// To delete single row of data from database *ATTEMPT*

    fun deleteRow(id: CalculatorActivity) {
        // Get the writable database
        val db = this.writableDatabase

        // Define the condition (WHERE clause)
        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())

        // Perform the delete operation
        db.delete("your_table_name", whereClause, whereArgs)

    } // Function to delete A SINGLE ROW

}