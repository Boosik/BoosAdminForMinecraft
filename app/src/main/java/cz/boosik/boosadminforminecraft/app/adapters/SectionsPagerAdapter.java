package cz.boosik.boosadminforminecraft.app.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlDynmapFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPlayersFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlPluginsFragment;
import cz.boosik.boosadminforminecraft.app.fragments.ServerControlServerFragment;

import java.util.Locale;

/**
 * Adapter for section pager
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private int count;

    /**
     * Default constructor for sections adapter
     * @param context Context of adapter
     * @param fm Fragment manager
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
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
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return context.getString(R.string.title_section3).toUpperCase(l);
            case 3:
                return context.getString(R.string.title_section4).toUpperCase(l);
        }
        return null;
    }
}
