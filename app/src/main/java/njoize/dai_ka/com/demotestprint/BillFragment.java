package njoize.dai_ka.com.demotestprint;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


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

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewBill);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        final ArrayList<String> zoneStringArrayList = new ArrayList<>();
        final ArrayList<String> deskStringArrayList = new ArrayList<>();
        ArrayList<String> detail1StringArrayList = new ArrayList<>();
        ArrayList<String> detail2StringArrayList = new ArrayList<>();
        ArrayList<String> detail3StringArrayList = new ArrayList<>();
        final ArrayList<String> idBillStringArrayList = new ArrayList<>();

        final ArrayList<String> cnumStringArrayList = new ArrayList<>();
        final ArrayList<String> typeStringArrayList = new ArrayList<>();
        final ArrayList<String> nameStringArrayList = new ArrayList<>();
        final ArrayList<String> timeStringArrayList = new ArrayList<>();

        try {

            ReadAllDataThread readAllDataThread = new ReadAllDataThread(getActivity());
            readAllDataThread.execute(myConstant.getUrlBillWhereOrder());
            String jsonString = readAllDataThread.get();
            Log.d(tag, "jsonString ==> " + jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i += 1) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                zoneStringArrayList.add("Zone " + jsonObject.getString("tzname"));
                zoneStringArrayList.add(jsonObject.getString("tzname"));
                deskStringArrayList.add(jsonObject.getString("tname"));
                detail1StringArrayList.add(jsonObject.getString("TotalList") + " รายการ " + jsonObject.getString("TotalPrice") + " บาท");
                detail2StringArrayList.add(jsonObject.getString("date") + " ลูกค้า " + jsonObject.getString("cnum") + " คน " + jsonObject.getString("type"));
                detail3StringArrayList.add("โดย " + jsonObject.getString("name"));
                idBillStringArrayList.add(jsonObject.getString("id"));
                timeStringArrayList.add(jsonObject.getString("date"));
                cnumStringArrayList.add(jsonObject.getString("cnum"));
                typeStringArrayList.add(jsonObject.getString("type"));
                nameStringArrayList.add(jsonObject.getString("name"));

            } // for

            BillRecyclerViewAdapter billRecyclerViewAdapter = new BillRecyclerViewAdapter(getActivity(),
                    zoneStringArrayList, deskStringArrayList, detail1StringArrayList,
                    detail2StringArrayList, detail3StringArrayList, new OnClickItem() {
                @Override
                public void onClickItem(View view, int positions) {
                    Log.d("2decV2", "You Click ==> " + positions);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.contentServiceFragment,
                                    BillDetailFragment.billDetailInstance(idBillStringArrayList.get(positions),
                                            timeStringArrayList.get(positions),
                                            cnumStringArrayList.get(positions),
                                            typeStringArrayList.get(positions),
                                            nameStringArrayList.get(positions),
                                            zoneStringArrayList.get(positions),
                                            deskStringArrayList.get(positions)))
                            .addToBackStack(null)
                            .commit();

                }
            });
            recyclerView.setAdapter(billRecyclerViewAdapter);


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
