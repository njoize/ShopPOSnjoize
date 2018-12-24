package njoize.dai_ka.com.demotestprint;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.wfsdk.WifiCommunication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillDetailFragment extends Fragment {

    //    Explicit
    private WifiCommunication wifiCommunication;
    private boolean aBoolean = false;
    private boolean communicationABoolean = true; // true ==> Can Print, false ==> Disable Print
    private Button button, printAgainButton;
    private int anInt = 0;
    private int total;

    private ArrayList<String> nameStringArrayList, numStringArrayList, priceStringArrayList;


    private String idBillString, timeString, cnumString, typeString, nameString, zoneString, deskString;
    private String tag = "2decV2";
    private MyConstant myConstant = new MyConstant();


    public BillDetailFragment() {
        // Required empty public constructor
    }

    public static BillDetailFragment billDetailInstance(String idString,
                                                        String timeString,
                                                        String cnumString,
                                                        String typeString,
                                                        String nameString,
                                                        String zoneString,
                                                        String deskString) {

        BillDetailFragment billDetailFragment = new BillDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idBill", idString);
        bundle.putString("Time", timeString);
        bundle.putString("cnum", cnumString);
        bundle.putString("type", typeString);
        bundle.putString("name", nameString);
        bundle.putString("Zone", zoneString);
        bundle.putString("Desk", deskString);
        billDetailFragment.setArguments(bundle);
        return billDetailFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Check Connected Printer
        createCommunicationPrinter();

//        Print Controller
        printController();


//        Get OID
        getOID();

//        Create Detail
        createDetail();

//        Show Text
        showText();


    } // Main Method

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbarDetail);
        ((DetailActivity) getActivity()).setSupportActionBar(toolbar);
        ((DetailActivity) getActivity()).getSupportActionBar().setTitle("Detail");
        ((DetailActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void printController() {
        button = getView().findViewById(R.id.btnPayment);
        printAgainButton = getView().findViewById(R.id.btnPaymentAgain);
        printAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCommunicationPrinter();
                communicationABoolean = true;
            }
        });
    }

    private void showText() {
        TextView leftTextView = getView().findViewById(R.id.txtLeft);
        TextView rightTextView = getView().findViewById(R.id.txtRight);

        leftTextView.setText(timeString + " ลูกค้า " + cnumString + " คน " + typeString + " โดย " + nameString);
        rightTextView.setText(zoneString + " " + "โต๊ะ " + deskString);


    }

    private void createDetail() {

        RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewBillDetail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        nameStringArrayList = new ArrayList<>();
        ArrayList<String> detailStringArrayList = new ArrayList<>();
        numStringArrayList = new ArrayList<>();
        ArrayList<String> amountStringArrayList = new ArrayList<>();
        ArrayList<String> billStringArrayList = new ArrayList<>();
        priceStringArrayList = new ArrayList<>();


        try {

            GetDtailBillWhereID getDtailBillWhereID = new GetDtailBillWhereID(getActivity());
            getDtailBillWhereID.execute(idBillString, myConstant.getUrlBillDetailWhereOID());
            String jsonString = getDtailBillWhereID.get();
            Log.d(tag, jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i += 1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                nameStringArrayList.add(jsonObject.getString("pname"));
                detailStringArrayList.add(jsonObject.getString("des"));
                numStringArrayList.add(jsonObject.getString("num"));
                amountStringArrayList.add("ราคา " + jsonObject.getString("price") + " บาท จำนวน " + jsonObject.getString("num"));
                billStringArrayList.add(jsonObject.getString("setpr"));
                priceStringArrayList.add(jsonObject.getString("sumPrice"));
//                priceStringArrayList.add(jsonObject.getString("price") + ".-");
            }

            BillDetailAdapter billDetailAdapter = new BillDetailAdapter(getActivity(), nameStringArrayList,
                    detailStringArrayList, amountStringArrayList, billStringArrayList, priceStringArrayList);
            recyclerView.setAdapter(billDetailAdapter);


            total = 0;
            for (String s : priceStringArrayList) {
                total = total + Integer.parseInt(s.trim());
            }

            TextView textView = getView().findViewById(R.id.txtTotal);
            textView.setText("ยอดสุทธิ " + Integer.toString(total) + " บาท");


        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(tag, "e at createDetail ==> " + e.toString());
        }

    }

    private void getOID() {
        idBillString = getArguments().getString("idBill");
        timeString = getArguments().getString("Time");
        cnumString = getArguments().getString("cnum");
        typeString = getArguments().getString("type");
        nameString = getArguments().getString("name");
        zoneString = getArguments().getString("Zone");
        deskString = getArguments().getString("Desk");
        Log.d(tag, "idBill ==> " + idBillString);
    }


    private void createCommunicationPrinter() {
        MyConstant myConstant = new MyConstant();
        wifiCommunication = new WifiCommunication(handler);
        wifiCommunication.initSocket(myConstant.getIpAddressPrinter(), myConstant.getPortPrinter());
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String tag = "12decV2";
            switch (msg.what) {

                case WifiCommunication.WFPRINTER_CONNECTED:
                    Log.d(tag, "Success Connected Printer");
                    button.setText("ชำระเงิน");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (communicationABoolean) {

                                anInt += 1;

                                String printSring = "Print";
                                printSring = printSring + " " + Integer.toString(anInt);
                                Log.d("12decV1", "You Click Payment: " + printSring);


                                byte[] top = new byte[]{0x10, 0x04, 0x04}; // Space Front Bill
                                //byte[] bytes  = new byte[]{0x1B, 0x4A, 40}; // Space Front Bill n*0.125 mm
                                byte[] lineup = new byte[]{0x0A, 0x0D}; // Update Line
                                byte[] centered = new byte[]{0x1B, 0x61, 1}; // centered
                                byte[] left = new byte[]{0x1B, 0x61, 0}; // left
                                byte[] right = new byte[]{0x1B, 0x61, 2}; // right
                                byte[] tab = new byte[]{27, 101, 0, 9}; // tab
                                byte[] dfont = new byte[]{0x1B, 0x21, 0x00}; // default font
                                byte[] bold = new byte[]{0x1B, 0x45, 0x01}; // bold
                                byte[] dbold = new byte[3]; // Set the font (double height and width bold)
                                dbold[0] = 0x1B;
                                dbold[1] = 0x21;
                                dbold[2] |= 0x04; // 08 04 bold
                                dbold[2] |= 0x08; // 10 08 height
                                dbold[2] |= 0x20; // 20 10
                                byte[] openCashDrawer = new byte[]{0x1B, 0x70, 0x00, 0x40, 0x50}; // Open Cash Drawer
                                byte[] cutterPaper = new byte[]{0x1D, 0x56, 0x42, 90}; // Cutter Paper command



                                wifiCommunication.sndByte(openCashDrawer);

//                                wifiCommunication.sndByte(top);
                                wifiCommunication.sndByte(dbold);
                                wifiCommunication.sndByte(centered);
                                wifiCommunication.sendMsg("Brainwake", "tis-620");
                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("Matichon Academy", "tis-620");
                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("02 003 4511", "tis-620");
                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sndByte(dfont);
                                wifiCommunication.sendMsg("-------------------------", "tis-620");
                                wifiCommunication.sndByte(lineup);

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("   REG  01", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightLongWord(nameString + "  "), "tis-620");
                                wifiCommunication.sndByte(lineup);

                                wifiCommunication.sendMsg("   " + "24/12/2018", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightLongWord(timeString + "  "), "tis-620");
                                wifiCommunication.sndByte(lineup);

                                wifiCommunication.sendMsg("  Table No. " + deskString + "  " + zoneString , "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(cnumString+"CT "), "tis-620");
//                                wifiCommunication.sendMsg("CT", "tis-620");
                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sndByte(lineup);

//                                Work Here

                                Log.d("12decV1", "numArray ==> " + numStringArrayList.toString());
                                Log.d("12decV1", "nameArray ==> " + nameStringArrayList.toString());
                                Log.d("12decV1", "priceArray ==> " + priceStringArrayList.toString());



                                for (int i = 0; i < nameStringArrayList.size(); i += 1) {

                                    wifiCommunication.sndByte(left);
//                                    wifiCommunication.sendMsg(Integer.toString(i + 1) + " x ", "tis-620");
                                    wifiCommunication.sendMsg("   " + numStringArrayList.get(i) + " x ", "tis-620");
                                    wifiCommunication.sndByte(tab);

                                    wifiCommunication.sendMsg(shortFood(nameStringArrayList.get(i)), "tis-620");
                                    wifiCommunication.sndByte(tab);

//                                    wifiCommunication.sendMsg("80", "tis-620");
                                    wifiCommunication.sndByte(tab);


                                    wifiCommunication.sendMsg(rightWord(priceStringArrayList.get(i)), "tis-620");
                                    wifiCommunication.sndByte(lineup);


                                }

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("   SUB TOTAL", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("   Discount", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("   Vat 7%", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("   Service Charge 10%", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");


                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sndByte(bold);
                                wifiCommunication.sendMsg("   ", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(bold);
                                wifiCommunication.sendMsg("TOTAL", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(dbold);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sndByte(dfont);
                                wifiCommunication.sendMsg("   CASH", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sendMsg("   Change", "tis-620");
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sndByte(tab);
                                wifiCommunication.sendMsg(rightWord(Integer.toString(total)), "tis-620");

                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sndByte(lineup);
                                wifiCommunication.sndByte(dbold);
                                wifiCommunication.sndByte(centered);
                                wifiCommunication.sendMsg("THANK YOU", "tis-620");
                                wifiCommunication.sndByte(lineup);

                                wifiCommunication.sndByte(cutterPaper);


                                wifiCommunication.close();

                                communicationABoolean = false;

                            } else {

                                //Log.d("24novV3", "Communication Disible");
                                Toast.makeText(getActivity(), "Disable Printer Please Press Click Again", Toast.LENGTH_SHORT).show();
                            }

                        } // onClick
                    });

                    break;
                case WifiCommunication.WFPRINTER_DISCONNECTED:
                    Log.d(tag, "Disconnected Printer");
                    break;
                default:
                    break;

            } // switch

        } // handleMessage
    };

    private String rightLongWord(String rightLongString) {

        int currentWord = rightLongString.length();
        String result = "";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < (15 - currentWord); i += 1) {
            stringBuilder.append(" ");
        }

        result = stringBuilder.toString() + rightLongString;

        return result;

    }


    private String shortTotal() {

        String s = "รวมทั้งสิ้น ";
        String s1 = " บาท";
        String result = s + Integer.toString(total) + s1;

        return result;
    }

    private String rightWord(String rightString) {

        int currentWord = rightString.length();
        String result = "";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < (5 - currentWord); i += 1) {
            stringBuilder.append(" ");
        }

        result = stringBuilder.toString() + rightString;

        return result;
    }

    private String shortFood(String foodString) {

        String result = foodString;

        if (result.length() >= 23) {
            result = result.substring(0, 20) + "...";
        } else {

            int currentWord = result.length();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < (23 - currentWord); i += 1) {
                stringBuilder.append(" ");
            }


            result = result + stringBuilder.toString();

        }

        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_detail, container, false);
    }

}