package app.com.gitkill.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import app.com.gitkill.R;
import app.com.gitkill.fragments.details.FollowersFragment;
import app.com.gitkill.fragments.details.FollowingFragment;

public class DetailsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private String userName;

    public DetailsFragmentPagerAdapter(FragmentManager fm, Context context, String userName) {
        super(fm);
        this.context = context;
        this.userName = userName;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) return FollowersFragment.getInstance(userName);
        else if (position == 1) return FollowingFragment.getInstance(userName);
        else return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return context.getString(R.string.followers);
            case 1:
                return context.getString(R.string.following);
            default:
                return null;
        }
    }
}
