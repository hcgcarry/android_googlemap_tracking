package com.example.googlemap;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class firebaseInsert extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private void writeNewLocation(String index, String longitude, String latitidue) {
        Location location= new Location(longitude,latitidue);

        this.database.child("Location").child(index).setValue(location);
    }


    private String TAG="MapsActivityClass";
    protected firebaseInsert() {
        writeNewLocation("1", "120.219413","22.990825");

        // Write a message to the database
        //DatabaseReference myRef = database.getReference("message");

        /*
        myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        */

    }
}