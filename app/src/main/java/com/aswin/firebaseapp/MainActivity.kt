package com.aswin.firebaseapp

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aswin.firebaseapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        /**For getting SHA1 value of the project**/
        //getSHA1()

        /** RealTime Database Reference **/
        // https://kotlin-firebaseapp-default-rtdb.firebaseio.com/  // This is the reference
        database = Firebase.database.reference

        /**Write Data to Firebase RealTime Database**/
        //writeSimpleData()

        /**Read Simple Data from Firebase RealTime Database**/
        //ReadSimpleData()

        /**Write Custom Object to Firebase RealTime Database**/
        //CreateCustomObject()

        /**Read Custom Object from Firebase RealTime Database**/
        ReadCustomObject()

    }
    /**Write Simple Data to Firebase RealTime Database**/
    fun writeSimpleData(){
        database.child("price").setValue("1940 $")
    }

    /**Read Simple Data from Firebase RealTime Database**/
    fun ReadSimpleData() {
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val gold_price = snapshot.value
                binding.tvResult.text = gold_price.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.child("price").addValueEventListener(postListener)
    }

    /**Write Custom Object to Firebase RealTime Database**/
    fun CreateCustomObject(){
        val user1 = User("Aswin", "123")
        database.child("Users").setValue(user1)
    }

    /**Read Custom Object from Firebase RealTime Database**/
    fun ReadCustomObject() {
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val u1 = snapshot.getValue<User>()
                //binding.tvResult.text = u1.toString()
                binding.tvResult.text = "Name : ${u1?.userName.toString()}  \nPassword : ${u1?.password.toString()}"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        database.child("Users").addValueEventListener(postListener)
    }

    /**For getting SHA1 value of the project**/
    fun getSHA1() {
        try {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())
                val sha1 = md.digest()

                // Convert byte array to hex string with colons between each byte
                val hexString = sha1.joinToString(separator = ":") { byte ->
                    "%02X".format(byte) // %02X formats to uppercase hex
                }

                Log.d("SHA1", hexString) // Print the SHA-1 hash in the desired format
            }
        } catch (e: Exception) {
            Log.e("SHA1", "Error: ${e.message}")
        }
    }
}