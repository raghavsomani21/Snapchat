package com.raghavsomani.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth

class snapsActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance();

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater;
        inflater.inflate(R.menu.snapchat_menu,menu);

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item?.itemId == R.id.createSnap){
            val intent = Intent(this,CreateSnapActivity::class.java);
            startActivity(intent);
        }

        if(item?.itemId == R.id.logOut){
            mAuth.signOut();
            finish();
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        mAuth.signOut();
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
    }
}
