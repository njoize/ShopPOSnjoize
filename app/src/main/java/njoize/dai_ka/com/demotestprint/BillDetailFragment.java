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
public class BillDetailFragment extends Fragment {

    private String idBillString, timeString, zoneString, deskString;
    private String tag = "2decV2";
    private MyConstant myConstant = new MyConstant();


    public BillDetailFragment() {
        // Required empty public constructor
    }

    public static BillDetailFragment billDetailInstance(String idString,
                                                        String timeString,
                                                        String zoneString,
                                                        String deskString) {

        BillDetailFragment billDetailFragment = new BillDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idBill", idString);
        bundle.putString("Time", timeString);
        bundle.putString("Zone", zoneString);
        bundle.putString("Desk", deskString);
        billDetailFragment.setArguments(bundle);
        return billDetailFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Get OID
        getOID();

//        Create Detail
        createDetail();

//        Show Text
        showText();



    } // Main Method

    private void showText() {
        TextView leftTextView = getView().findViewById(R.id.txtLeft);
        TextView rightTextView = getView().findViewById(R.id.txtRight);

        leftTextView.setText(timeString + " " + "Other");
        rightTextView.setText("Zone " + zoneString + " " + "Desk " + deskString);


    }

    private void createDetail() {

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewBillDetail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<String> nameStringArrayList = new ArrayList<>();
        ArrayList<String> detailStringArrayList = new ArrayList<>();
        ArrayList<String> amountStringArrayList = new ArrayList<>();
        ArrayList<String> billStringArrayList = new ArrayList<>();
        ArrayList<String> priceStringArrayList = new ArrayList<>();


        try {

            GetDtailBillWhereID getDtailBillWhereID = new GetDtailBillWhereID(getActivity());
            getDtailBillWhereID.execute(idBillString, myConstant.getUrlBillDetailWhereOID());
            String jsonString = getDtailBillWhereID.get();
            Log.d(tag, jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nameStringArrayList.add(jsonObject.getString("name"));
                detailStringArrayList.add(jsonObject.getString("des"));
                amountStringArrayList.add(jsonObject.getString("num"));
                billStringArrayList.add(jsonObject.getString("setpr"));
                priceStringArrayList.add(jsonObject.getString("price"));
            }

            BillDetailAdapter billDetailAdapter = new BillDetailAdapter(getActivity(), nameStringArrayList,
                    detailStringArrayList, amountStringArrayList, billStringArrayList, priceStringArrayList);
            recyclerView.setAdapter(billDetailAdapter);


            int total = 0;
            for (String s : priceStringArrayList) {
                total = total + Integer.parseInt(s.trim());
            }

            TextView textView = getView().findViewById(R.id.txtTotal);
            textView.setText("Total = " + Integer.toString(total) + "THB.");


        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(tag, "e at createDetail ==> " + e.toString());
        }

    }

    private void getOID() {
        idBillString = getArguments().getString("idBill");
        timeString = getArguments().getString("Time");
        zoneString = getArguments().getString("Zone");
        deskString = getArguments().getString("Desk");
        Log.d(tag, "idBill ==> " + idBillString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_detail, container, false);
    }

}
