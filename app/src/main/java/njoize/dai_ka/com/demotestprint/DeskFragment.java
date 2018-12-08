package njoize.dai_ka.com.demotestprint;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeskFragment extends Fragment {

    //    Explicit
    private ImageView[][] imageViews = new ImageView[5][10];
    private int[][] ints = new int[][]{
            {R.id.imv0_0, R.id.imv0_1, R.id.imv0_2, R.id.imv0_3, R.id.imv0_4, R.id.imv0_5, R.id.imv0_6, R.id.imv0_7, R.id.imv0_8, R.id.imv0_9},
            {R.id.imv1_0, R.id.imv1_1, R.id.imv1_2, R.id.imv1_3, R.id.imv1_4, R.id.imv1_5, R.id.imv1_6, R.id.imv1_7, R.id.imv1_8, R.id.imv1_9},
            {R.id.imv2_0, R.id.imv2_1, R.id.imv2_2, R.id.imv2_3, R.id.imv2_4, R.id.imv2_5, R.id.imv2_6, R.id.imv2_7, R.id.imv2_8, R.id.imv2_9},
            {R.id.imv3_0, R.id.imv3_1, R.id.imv3_2, R.id.imv3_3, R.id.imv3_4, R.id.imv3_5, R.id.imv3_6, R.id.imv3_7, R.id.imv3_8, R.id.imv3_9},
            {R.id.imv4_0, R.id.imv4_1, R.id.imv4_2, R.id.imv4_3, R.id.imv4_4, R.id.imv4_5, R.id.imv4_6, R.id.imv4_7, R.id.imv4_8, R.id.imv4_9}

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
        drawDesk(1,1,4,2);



    } // Main Method


    private void drawDesk(int startX, int startY, int endX, int endY) {

        for (int i = startX; i <= endX; i += 1) {

            for (int i1 = startY; i1 <= endY; i1 += 1) {

                addRed(i, i1);

            } // for Y

        } // for X

    }

    private void addRed(int indexX, int indexY) {
        imageViews[indexX][indexY].setBackgroundColor(Color.RED);
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
