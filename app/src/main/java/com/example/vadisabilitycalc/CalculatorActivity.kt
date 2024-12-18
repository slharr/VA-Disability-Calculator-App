package com.example.vadisabilitycalc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.vadisabilitycalc.databinding.ActivityCalculatorBinding

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
//        val clickedNextItem = findViewById<Button>(R.id.nextItem)
//        val clickedSubmit = findViewById<Button>(R.id.submitData)
//        val disabilityName = findViewById<TextView>(R.id.disabilityName)
//        val disabilityPercentage = findViewById<TextView>(R.id.disabilityPercentage)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calculator)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // using the above two lines is necessary to get the Buttons to work properly

        databaseHelper = DatabaseHelper(this)
        listView = findViewById(R.id.disability_list_view)

        disabilitiesToList()

        binding.nextItem.setOnClickListener{
            Toast.makeText(this, "I'm Working!", Toast.LENGTH_SHORT).show()
            val disName = binding.disabilityName.text.toString()
            val disPercentage = binding.disabilityPercentage.text.toString()
            // the above values are from the text boxes and given a new variable name
            if(disName.isNotBlank() && disPercentage.isNotBlank()){
                databaseHelper.insertDisabilities(disName, disPercentage.toDouble())
                // These new variables are passed into the function registrationToDatabase

                binding.disabilityName.text.clear()
                binding.disabilityPercentage.text.clear()
                //hideKeyboard()
            }else{
                Toast.makeText( this, "You must enter something!", Toast.LENGTH_SHORT).show()
            }
            //editTextUpdate.visibility = View.GONE
        }// function to add items into database from text boxes
    }

    private fun disabilitiesToList(){

        val cursor = databaseHelper.getAllDataFromDisabilities()
        val list = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DISABILITY))
                val value = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PERCENTAGE))
                list.add("$name: $value")
            } while (cursor.moveToNext())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        // The ArrayAdapter is responsible for converting the data in your list (combinedList) into views that can be displayed in the ListView.
        listView.adapter = adapter
        // This connects the values from the adapter and places it into the listView.
    }


//    private fun hideKeyboard() {
//        val view = this.currentFocus
//        if(view != null){
//            val key = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            key.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }// Function to create a keyboard to use


    private fun insertDisabilityData(){
        val disName = binding.disabilityName.text.toString()
        val disPercentage = binding.disabilityPercentage.text.toString()

        if(disName.isNotBlank() && disPercentage.isNotBlank()){
            val id = databaseHelper.insertDisabilities(disName, disPercentage.toDouble())

            if(id > 0){
                binding.disabilityName.text.clear()
                binding.disabilityPercentage.text.clear()
                // Clears the disability name and disability percentage values from the textboxes.
            }

        }
    } // end insertData class

    fun insertData(name: String, value: String) {
        val db = databaseHelper.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(DatabaseHelper.COLUMN_DISABILITY, name)
        contentValues.put(DatabaseHelper.COLUMN_PERCENTAGE, value)
        db.insert(DatabaseHelper.TABLE_DISABILITY, null, contentValues)
        db.close()
    } // Another way to insert disability values into database

    private fun disabilitiesToDatabase(disability: String, percentage: Double){
        val insertedRowId = databaseHelper.insertDisabilities(disability,percentage.toDouble())
        if( insertedRowId != -1L){
            Toast.makeText( this, "Disability applied!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Input Failed!", Toast.LENGTH_SHORT).show()
        }
    }//Another function to insert disability data into database

} // end main class