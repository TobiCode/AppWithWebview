package com.example.tobi.billingapp2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tobi.billingapp2.Utility.CSVFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //In an Activity
    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "/Gemuese-Files");
    private String mChosenFile;
    private static final String FTYPE = ".csv";
    private static final int DIALOG_LOAD_FILE = 1000;
    private String pathString;
    ArrayList<String[]> rowList;
    String[][] csvData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();

        ArrayList<String[]> rowList = new ArrayList<>();

        pathString = Environment.getExternalStorageDirectory().toString() + "/Gemuese-Files";


        Log.i("Debug: ", Environment.getExternalStorageDirectory().toString());

        loadFileList();


    }

    public ArrayList<String[]> getRowList() {
        return rowList;
    }

    public void setRowList(ArrayList<String[]> rowList) {
        this.rowList = rowList;
    }

    public void openExplorer(View view) {
        onCreateDialog(1000);
    }

    private void loadFileList() {
        try {
            mPath.mkdirs();
            Log.i("Debug: ", "Folder Created");
        } catch (SecurityException e) {
            Log.i("Gemuese-Files: ", "unable to write on the sd card " + e.toString());
        }
        if (mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }

            };
            mFileList = mPath.list(filter);
        } else {
            mFileList = new String[0];
        }
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if (mFileList == null) {
                    Log.i("Gemuese-Files: ", "Showing file picker before loading the file list");
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenFile = mFileList[which];
                        //load the File
                        try {
                            InputStream inputStream =
                                    new FileInputStream(pathString + "/" + mChosenFile);
                            CSVFile csvFile = new CSVFile(inputStream);
                            rowList = (ArrayList) csvFile.read();
                            csvData = castArrayListTo2D(rowList);

                            /*
                            for (String[] row : rowList) {
                                for (int i = 0; i < row.length; i++) {
                                    System.out.println("Item " + i + " :" + row[i].toString());
                                }
                            } */
                            //start CSV_Content
                            Intent intent = new Intent(getApplicationContext(), CSV_Content.class);
                            intent.putExtra("rowList", rowList);
                            intent.putExtra("csvData", csvData);
                            startActivity(intent);



                        } catch (FileNotFoundException e) {
                            e.printStackTrace();

                        }
                        System.out.println("File Loaded: " + mFileList[which]);
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }

    private String[][] castArrayListTo2D(ArrayList<String[]> rowList){
        String[][] result = new String[rowList.size()][rowList.get(0).length];
        for (int i = 0; i < rowList.size(); i++) {
            result[i] = rowList.get(i);
        }
        return  result;
    }


    //Methods to check if user has permissions:
    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission: ", "Permission is granted1");
                return true;
            } else {

                Log.i("Permission: ", "Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.i("Permission: ", "Permission is granted1");
            return true;
        }
    }

    public boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission: ", "Permission is granted2");
                return true;
            } else {

                Log.i("Permission: ", "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.i("Permission: ", "Permission is granted2");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.i("Permission: ", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission: ", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    Toast.makeText(this, "Click Button again", Toast.LENGTH_SHORT).show();
                } else {
                    System.exit(0);
                }
                break;

            case 3:
                Log.i("Permission: ", "External storage1");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission: ", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    Toast.makeText(this, "Click Button again", Toast.LENGTH_SHORT).show();
                } else {
                    System.exit(0);
                }
                break;
        }
    }
}