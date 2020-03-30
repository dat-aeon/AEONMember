package mm.com.aeon.vcsaeon.adapters;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import mm.com.aeon.vcsaeon.fragments.CouponTabFragment;
import mm.com.aeon.vcsaeon.fragments.EventNewsTabFragment;
import mm.com.aeon.vcsaeon.fragments.PromotionTabFragment;

public class EventsAndNewsAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public EventsAndNewsAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs=numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                EventNewsTabFragment tab3 = new EventNewsTabFragment();
                return tab3;
            /*case 1:
                PromotionTabFragment tab1 = new PromotionTabFragment();
                return tab1;*/
            case 1:
                CouponTabFragment tab2 = new CouponTabFragment();
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
