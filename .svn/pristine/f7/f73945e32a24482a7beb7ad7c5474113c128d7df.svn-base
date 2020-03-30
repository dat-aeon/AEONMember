package mm.com.aeon.vcsaeon.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import mm.com.aeon.vcsaeon.fragments.HotlinePhoneTabFragment;
import mm.com.aeon.vcsaeon.fragments.MessagingTabFragment;


public class ContactUsAdapater extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public static HotlinePhoneTabFragment tab1;
    public static MessagingTabFragment tab2;

    public ContactUsAdapater(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                tab1 = new HotlinePhoneTabFragment();
                return tab1;
            case 1:
                tab2 = new MessagingTabFragment();
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