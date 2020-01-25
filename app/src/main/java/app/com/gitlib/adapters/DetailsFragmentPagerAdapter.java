package app.com.gitlib.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import app.com.gitlib.R;
import app.com.gitlib.fragments.details.FollowersFragment;
import app.com.gitlib.fragments.details.FollowingFragment;

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
