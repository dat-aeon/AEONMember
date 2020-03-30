package mm.com.aeon.vcsaeon.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import mm.com.aeon.vcsaeon.fragments.NavSecQAInfoUpdateTabFragment;
import mm.com.aeon.vcsaeon.fragments.NavUserInfoUpdateTabFragment;

public class UpdateInformationAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public UpdateInformationAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                NavSecQAInfoUpdateTabFragment tab1 = new NavSecQAInfoUpdateTabFragment();
                return tab1;
            case 1:
                NavUserInfoUpdateTabFragment tab2 = new NavUserInfoUpdateTabFragment();
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
