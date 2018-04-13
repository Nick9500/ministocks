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
        String fileLocation = Environment.getExternalStorageDirectory() + File.separator + "DataFolder";
        File filePath = new File(fileLocation);
        filePath.mkdirs();

        //file name
        File fileSavedOnDevice = new File(filePath, "stockData.csv");

        try {
            final boolean newFile = fileSavedOnDevice.createNewFile();
            FileOutputStream fOut = new FileOutputStream(fileSavedOnDevice);
            OutputStreamWriter writeTOFile = new OutputStreamWriter(fOut);

            List<WidgetRow> dataList = GlobalWidgetData.getList();

            for (int i = 0; i < dataList.size(); i++) {
                writeTOFile.append(dataList.get(i).getSymbol());
                writeTOFile.append("\n");
            }
            writeTOFile.close();

            fOut.flush();
            fOut.close();

        } catch (IOException e) {
            Log.e("Exception", "File write to device storage failed: " + e.toString());
        }
        return null;
    }
}
