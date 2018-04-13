package nitezh.ministock;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import nitezh.ministock.activities.GlobalWidgetData;
import nitezh.ministock.activities.widget.WidgetRow;

/**
 * Created by raj34 on 2018-04-13.
 */

public class SaveFile extends AsyncTask<Void, Void,Void> {

    public SaveFile(){

    }

    protected Void doInBackground(Void... voids) {
        String path = Environment.getExternalStorageDirectory() + File.separator + "DataFolder";
        File folder = new File(path);
        folder.mkdirs();

        //file name
        File file = new File(folder, "stockData.csv");

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter outWriter = new OutputStreamWriter(fOut);

            List<WidgetRow> myList = GlobalWidgetData.getList();
            for (int i = 0; i < myList.size(); i++) {
                outWriter.append(myList.get(i).getSymbol());
                outWriter.append("\n");
            }
            outWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return null;
    }
}
