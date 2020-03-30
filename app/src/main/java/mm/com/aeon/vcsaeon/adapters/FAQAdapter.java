package mm.com.aeon.vcsaeon.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import mm.com.aeon.vcsaeon.delegates.LanguageChangeListener;
import mm.com.aeon.vcsaeon.fragments.AboutUsTabFragment;
import mm.com.aeon.vcsaeon.fragments.FAQTabFragment;
import mm.com.aeon.vcsaeon.fragments.TermsAndConditionTabFragment;

public class FAQAdapter extends FragmentStatePagerAdapter {

    int numberOfTabs;

    public FAQAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FAQTabFragment tab1 = new FAQTabFragment();
                return tab1;
            case 1:
                TermsAndConditionTabFragment tab2 = new TermsAndConditionTabFragment();
                return tab2;
            case 2:
                AboutUsTabFragment tab3 = new AboutUsTabFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}