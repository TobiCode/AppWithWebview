package com.example.tobi.billingapp2.Utility;

/**
 * Created by Tobi on 24.06.2018.
 */
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobi on 22.06.2018.
 */
public class CSVFile {

    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public  List read(){
        List resultList = new ArrayList();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                csvLine = csvLine.replaceAll(",(?!(?:[^\"]*\"[^\"]*\")*[^\"]*$)" , "");
                System.out.println(csvLine);
                //csvLine = csvLine.replace("(?<=\\\")([^\"]+?),([^\"]+?)(?=\\\")", "");
                //,(?!(?:[^"]*"[^"]*")*[^"]*$)
                String[] row = csvLine.split(",");
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            Log.i("Error: ", "Not able to read CSV");
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }
        return resultList;
    }
}