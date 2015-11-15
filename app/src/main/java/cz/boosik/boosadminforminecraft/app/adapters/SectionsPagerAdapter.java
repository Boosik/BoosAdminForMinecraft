package cz.boosik.boosadminforminecraft.app.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlDynmapFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPlayersFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPluginsFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlServerFragment;

import java.util.Locale;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    ServerControlActivity activity;

    public SectionsPagerAdapter(ServerControlActivity activity, FragmentManager fm) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ServerControlServerFragment.newInstance(position + 1);
            case 1:
                return ServerControlPlayersFragment.newInstance(position + 1);
            case 2:
                return ServerControlPluginsFragment.newInstance(position + 1);
            case 3:
                return ServerControlDynmapFragment.newInstance(position + 1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return activity.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return activity.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return activity.getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return activity.getString(R.string.title_section4).toUpperCase(l);
        }
        return null;
    }
}
