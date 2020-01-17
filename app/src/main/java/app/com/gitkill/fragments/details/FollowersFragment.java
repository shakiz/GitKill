package app.com.gitkill.fragments.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.com.gitkill.R;

public class FollowersFragment extends Fragment {

    private static final FollowersFragment FOLLOWERS_FRAGMENT = null;

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        if (FOLLOWERS_FRAGMENT == null) return new FollowersFragment();
        else return FOLLOWERS_FRAGMENT;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_followers, container, false);init(view);
        bindUIWithComponents(view);
        return view;
    }

    private void init(View view) {

    }

    private void bindUIWithComponents(View view) {

    }

}
