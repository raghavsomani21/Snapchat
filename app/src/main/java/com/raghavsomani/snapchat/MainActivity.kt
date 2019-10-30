package com.raghavsomani.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var  mFirebaseAnalytics:FirebaseAnalytics?=null
    var emailEditText:EditText? = null
    var passwordEditText:EditText? = null
    var mAuth:FirebaseAuth = FirebaseAuth.getInstance();

    fun EncodeString(string: String): String {
        return string.replace(".", ",")
    }

    fun DecodeString(string: String): String {
        return string.replace(",", ".")
    }

    fun goButton(view: View) {
        //Log in existing user
        mAuth.signInWithEmailAndPassword(
            emailEditText?.text.toString(),
            passwordEditText?.text.toString()
        )
                .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("Log in","Successful");
                    logIn();
                }
                else {
                    //sign up a new user
                        mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(),passwordEditText?.text.toString()).addOnCompleteListener(this){task ->
                            if (task.isSuccessful) {
                                //Add to database
                                var email : String = EncodeString(emailEditText?.text.toString())
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user?.uid!!).child("email").child(email)
                                Log.i("Log in","Successful");
                                logIn();
                            }
                            else{
                                Toast.makeText(this,"Log in failed",Toast.LENGTH_LONG).show();
                                Log.i("log in failed:",task.exception.toString());
                            }
                        }

                }
            }
       }

    fun logIn(){
            val intent = Intent(this,snapsActivity::class.java)
            startActivity(intent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        if(mAuth.currentUser != null){
            logIn();
        }
    }
}
