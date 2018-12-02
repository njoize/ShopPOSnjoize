package njoize.dai_ka.com.demotestprint;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillFragment extends Fragment {

    private MyConstant myConstant = new MyConstant();
    private String tag = "2decV1";


    public BillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Check Status
        checkStatus();

//        Create RecyclerView
        createRecyclerView();

    } // Method Main

    private void createRecyclerView() {


        try {

            ReadAllDataThread readAllDataThread = new ReadAllDataThread(getActivity());
            readAllDataThread.execute(myConstant.getUrlTestReadAllData());
            String jsonString = readAllDataThread.get();
            Log.d(tag,"jsonString ==> " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    } // createRecyclerView

    private void checkStatus() {
        TextView title0TextView = getView().findViewById(R.id.txtTitle0);
        TextView title1TextView = getView().findViewById(R.id.txtTitle1);
        TextView title2TextView = getView().findViewById(R.id.txtTitle2);
        TextView title3TextView = getView().findViewById(R.id.txtTitle3);

        String[] strings = myConstant.getBillTitleStrings();
        title0TextView.setText(strings[0]);
        title1TextView.setText(strings[1]);
        title2TextView.setText(strings[2]);
        title3TextView.setText(strings[3]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

} // Main Class
