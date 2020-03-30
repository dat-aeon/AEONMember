package mm.com.aeon.vcsaeon.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import mm.com.aeon.vcsaeon.delegates.ValidateFragmentInterface;
import mm.com.aeon.vcsaeon.fragments.SmallLoanApplicationDataFragment;
import mm.com.aeon.vcsaeon.fragments.SmallLoanConfirmFragment;
import mm.com.aeon.vcsaeon.fragments.SmallLoanEmergencyFragment;
import mm.com.aeon.vcsaeon.fragments.SmallLoanGuarantorFragment;
import mm.com.aeon.vcsaeon.fragments.SmallLoanOccupationFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new SmallLoanApplicationDataFragment();
            case 1:
                return new SmallLoanOccupationFragment();
            case 2:
                return new SmallLoanEmergencyFragment();
            case 3:
                return new SmallLoanGuarantorFragment();
            case 4:
                return new SmallLoanConfirmFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof ValidateFragmentInterface) {
            ((ValidateFragmentInterface) object).validate();
        }
        return super.getItemPosition(object);
    }
}
