package nitezh.ministock;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GRao & nicholasfong on 4/4/2018.
 */

public class NewsFeedAdapter extends ArrayAdapter{

    //to reference the Activity
    private final Activity context;

    //to store the list of countries
    private final ArrayList<String> nameArray;

    //to store the list of countries
    private final ArrayList<String> infoArray;

    public NewsFeedAdapter(Activity context, ArrayList<String> nameArrayParam, ArrayList<String> infoArrayParam){

        super(context,R.layout.bonobo_news_row, nameArrayParam);
        this.context=context;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.bonobo_news_row, null,true);

        //this code gets references to objects in the bonobo_news_listView.xml file
        TextView nameTextField = (TextView) rowView.findViewById(R.id.newsNameTextID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.newsContentID);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(nameArray.get(position));
        infoTextField.setText(infoArray.get(position));

        return rowView;
    }
}
