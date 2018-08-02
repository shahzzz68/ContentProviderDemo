package com.example.shahz.contentproviderdemo;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<Cursor> {

    ContentResolver resolver ;
    Cursor cursor = null;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    TextView textView;
    String[] projections = new String[]

            {
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.Contacts.CONTACT_STATUS,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER
            };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted

                cursor(cursor);

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.READ_CONTACTS)) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);


        }
        else
        {
            resolver = getContentResolver();
            getLoaderManager().initLoader(1,null,  this);
        }




        /////////////////////////////////////// multiple requests///////////////////////////////////////////////////////////////////////

//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
//        {
//            if ((ActivityCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) &&
//                    (ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED))
//            {
//                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.CAMERA},1);
//            }
//        }

        


    }


//    public void showContacts() {
//
//        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
//                projections,
//                null,
//                null,
//                null);
//
//                 cursor();
//
//    }

    public void cursor(Cursor cursor)
    {

        TextView textView=(TextView)findViewById(R.id.textView);
        if (cursor != null && cursor.getCount() > 0) {
            StringBuilder stringBuilder = new StringBuilder("");
            while (cursor.moveToNext()) {
                stringBuilder.append(cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2) + "\n");
            }
            textView.setText(stringBuilder.toString());
        } else {
            textView.setText("no contacts");
        }
    }


    @Override
    public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        if (i==1)
        {
            return new CursorLoader(this,ContactsContract.Contacts.CONTENT_URI,
                    projections,
                    null,
                    null,
                    null);
        }
        return null;

    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {

      cursor(cursor);
      this.cursor=cursor;

      
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {

    }
}

