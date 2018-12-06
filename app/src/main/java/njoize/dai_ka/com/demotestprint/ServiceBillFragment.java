package njoize.dai_ka.com.demotestprint;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceBillFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyConstant myConstant = new MyConstant();


    public ServiceBillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Initail View
        tabLayout = getView().findViewById(R.id.tabLayout);
        String[] strings = myConstant.getBillTitleStrings();
        for (String myString : strings) {
            tabLayout.addTab(tabLayout.newTab().setText(myString));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    } // Main Method

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_bill, container, false);
    }

} // Main Class
