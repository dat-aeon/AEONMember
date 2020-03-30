package mm.com.aeon.vcsaeon.adapters;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import mm.com.aeon.vcsaeon.fragments.MemberCardOneTabFragment;
import mm.com.aeon.vcsaeon.fragments.MemberCardTwoTabFragment;

public class MemberCardAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;
    String language;

    public MemberCardAdapter(FragmentManager fm, int numberOfTabs, String language) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
        this.language = language;
    }

    @Override
    public Fragment getItem(int position) {
        Log.e("tab change to", position+" " + language);
        switch (position){
            case 0:
                MemberCardOneTabFragment tab1 = new MemberCardOneTabFragment();
                return tab1;
            case 1:
                MemberCardTwoTabFragment tab2 = new MemberCardTwoTabFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
