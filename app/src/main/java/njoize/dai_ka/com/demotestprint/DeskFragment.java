package njoize.dai_ka.com.demotestprint;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeskFragment extends Fragment {

    //    Explicit
    private TextView[][] imageViews = new TextView[10][10];
    private int[][] ints = new int[][]{
            {R.id.imv0_0, R.id.imv0_1, R.id.imv0_2, R.id.imv0_3, R.id.imv0_4, R.id.imv0_5, R.id.imv0_6, R.id.imv0_7, R.id.imv0_8, R.id.imv0_9},
            {R.id.imv1_0, R.id.imv1_1, R.id.imv1_2, R.id.imv1_3, R.id.imv1_4, R.id.imv1_5, R.id.imv1_6, R.id.imv1_7, R.id.imv1_8, R.id.imv1_9},
            {R.id.imv2_0, R.id.imv2_1, R.id.imv2_2, R.id.imv2_3, R.id.imv2_4, R.id.imv2_5, R.id.imv2_6, R.id.imv2_7, R.id.imv2_8, R.id.imv2_9},
            {R.id.imv3_0, R.id.imv3_1, R.id.imv3_2, R.id.imv3_3, R.id.imv3_4, R.id.imv3_5, R.id.imv3_6, R.id.imv3_7, R.id.imv3_8, R.id.imv3_9},
            {R.id.imv4_0, R.id.imv4_1, R.id.imv4_2, R.id.imv4_3, R.id.imv4_4, R.id.imv4_5, R.id.imv4_6, R.id.imv4_7, R.id.imv4_8, R.id.imv4_9},
            {R.id.imv5_0, R.id.imv5_1, R.id.imv5_2, R.id.imv5_3, R.id.imv5_4, R.id.imv5_5, R.id.imv5_6, R.id.imv5_7, R.id.imv5_8, R.id.imv5_9},
            {R.id.imv6_0, R.id.imv6_1, R.id.imv6_2, R.id.imv6_3, R.id.imv6_4, R.id.imv6_5, R.id.imv6_6, R.id.imv6_7, R.id.imv6_8, R.id.imv6_9},
            {R.id.imv7_0, R.id.imv7_1, R.id.imv7_2, R.id.imv7_3, R.id.imv7_4, R.id.imv7_5, R.id.imv7_6, R.id.imv7_7, R.id.imv7_8, R.id.imv7_9},
            {R.id.imv8_0, R.id.imv8_1, R.id.imv8_2, R.id.imv8_3, R.id.imv8_4, R.id.imv8_5, R.id.imv8_6, R.id.imv8_7, R.id.imv8_8, R.id.imv8_9},
            {R.id.imv9_0, R.id.imv9_1, R.id.imv9_2, R.id.imv9_3, R.id.imv9_4, R.id.imv9_5, R.id.imv9_6, R.id.imv9_7, R.id.imv9_8, R.id.imv9_9}

    };


    public DeskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Initial View
        initialView();

//      Draw Desk
        drawDesk();


    } // Main Method

    private void drawDesk() {

        String tag = "8decV2";

        try {

            MyConstant myConstant = new MyConstant();

            //joyce
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences(myConstant.getSharePreferFile(), Context.MODE_PRIVATE);
            String userLogined = sharedPreferences.getString("User", "");
            Log.d("8decV2", "userLogined ==> " + userLogined);
            //---

            GetAllData getAllData = new GetAllData(getActivity());
            getAllData.execute(myConstant.getUrlReadAllDesk());
            String jsonString = getAllData.get();
            Log.d("8decV2", "jsonString ==> " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i += 1) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String startDesk = jsonObject.getString("tbstart");
                String endDesk = jsonObject.getString("tbend");
                Log.d(tag, "startDesk ==> " + startDesk);
                Log.d(tag, "endDesk ==> " + endDesk);
                convasDesk(startDesk, endDesk);

            } // for


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void convasDesk(String startDesk, String endDesk) {

        String[] startStrings = startDesk.split("-");
        String[] endStrings = endDesk.split("-");
        boolean b = true;


        int startX = Integer.parseInt(startStrings[0].trim());
        int startY = Integer.parseInt(startStrings[1].trim());
        int endX = Integer.parseInt(endStrings[0].trim());
        int endY = Integer.parseInt(endStrings[1].trim());

        for (int i = startX; i <= endX; i += 1) {

            for (int i1 = startY; i1 <= endY; i1 += 1) {

                addRed(i, i1, b);
                b = false;

            } // for Y

        } // for X

    }

    private void addRed(int indexX, int indexY, boolean b) {
        imageViews[indexX][indexY].setBackgroundColor(Color.RED);

        if (b) {
            imageViews[indexX][indexY].setText("No.10");
            imageViews[indexX][indexY].setTextColor(Color.WHITE);
            imageViews[indexX][indexY].setTextSize(10);
        }

    }

    private void initialView() {
        for (int i = 0; i < 5; i += 1) {

            for (int i1 = 0; i1 < 10; i1 += 1) {

                imageViews[i][i1] = getView().findViewById(ints[i][i1]);

            } // for1

        } // for0
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_desk, container, false);
    }

}
