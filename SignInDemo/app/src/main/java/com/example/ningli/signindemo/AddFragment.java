package com.example.ningli.signindemo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ningli.signindemo.database.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class AddFragment extends Fragment {

    public static final String KEY_PAGE = "page";

    private static String USER_ID;

    private ImageView image;

    Uri photoURI;

    @NonNull
    public static AddFragment newInstance(int page, String NAME) {
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE, page);
        args.putString("USER_ID", NAME);
        USER_ID = NAME;

        AddFragment addFragment = new AddFragment();
        addFragment.setArguments(args);
        return addFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_add, container, false);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //image.setImageBitmap(imageBitmap);
            storeImage(imageBitmap);
            image.setImageURI(photoURI);

            //// TODO: store the puth to the pic

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        photoURI = Uri.fromFile(pictureFile);
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                //return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        image = (ImageView)view.findViewById(R.id.image);

        View camera = view.findViewById(R.id.camera_capture);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }

            }
        });



        View add = view.findViewById(R.id.AddButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText itemName = (EditText) getView().findViewById(R.id.AddItemName);
                String name = itemName.getText().toString();

                EditText itemNumber = (EditText) getView().findViewById(R.id.AddItemNumber);
                String num = itemNumber.getText().toString();


                final SQLiteOpenHelper db = DBHelper.getInstance(getActivity());
                SQLiteDatabase database = db.getWritableDatabase();

                if (name.isEmpty() || num.isEmpty() || photoURI == null)
                    Toast.makeText(getActivity(), "Invalid!", Toast.LENGTH_LONG).show();
                else {
                    ContentValues values = new ContentValues();
                    values.put("item", name);
                    values.put("num", Integer.valueOf(num));
                    values.put("state", 0);
                    values.put("image", photoURI.toString());

                    long id = database.insert(USER_ID, null, values);
                    itemName.setText("");
                    itemNumber.setText("");
                    Toast.makeText(getActivity(), "Adding item to List ... " + " ( " + name + ", " + num + ", " + photoURI.toString() + " ) " + "on " + id, Toast.LENGTH_LONG).show();
                    photoURI = null;
                    ((SuccessActivity) getContext()).pagerAdapter.notifyDataSetChanged();

                }
            }
        });

    }
}
