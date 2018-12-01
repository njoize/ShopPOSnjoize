package njoize.dai_ka.com.demotestprint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private FragmentManager fragmentManager;
    private int anInt;

    public MyPagerAdapter(FragmentManager fragmentManager, int anInt) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.anInt = anInt;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                break;
        }

        return null;
    }

    @Override
    public int getCount() {
        return anInt;
    }
} // Main Class
