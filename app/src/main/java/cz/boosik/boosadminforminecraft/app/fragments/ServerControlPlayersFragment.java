package cz.boosik.boosadminforminecraft.app.fragments;

/**
 * @author jakub.kolar@bsc-ideas.com
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cz.boosik.boosadminforminecraft.app.R;

public class ServerControlPlayersFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ServerControlPlayersFragment newInstance(int sectionNumber) {
        ServerControlPlayersFragment fragment = new ServerControlPlayersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ServerControlPlayersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_players, container, false);
        return rootView;
    }
}
