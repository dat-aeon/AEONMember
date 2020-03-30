package mm.com.aeon.vcsaeon.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import mm.com.aeon.vcsaeon.fragments.OutletListTabFragment;
import mm.com.aeon.vcsaeon.fragments.OutletLocationTabFragment;

public class FindNearOutletAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public FindNearOutletAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                OutletLocationTabFragment tab1 = new OutletLocationTabFragment();
                return tab1;
            case 1 :
                OutletListTabFragment tab2 = new OutletListTabFragment();
                return tab2;
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
