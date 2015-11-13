package cz.boosik.boosadminforminecraft.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import cz.boosik.boosadminforminecraft.app.R;
import cz.boosik.boosadminforminecraft.app.activities.ServerControlActivity;

/**
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerControlDynmapFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Bind(R.id.wvDynmap)
    WebView wvDynmap;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ServerControlDynmapFragment newInstance(int sectionNumber) {
        ServerControlDynmapFragment fragment = new ServerControlDynmapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ServerControlDynmapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_dynmap, container, false);
        ButterKnife.bind(this, rootView);
        if (ServerControlActivity.dynmapAvailable) {
            wvDynmap.setWebChromeClient(new WebChromeClient());
            wvDynmap.setWebViewClient(new WebViewClient());
            wvDynmap.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            WebSettings settings = wvDynmap.getSettings();
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setJavaScriptEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setAppCacheEnabled(false);
            settings.setDomStorageEnabled(true);
            settings.setPluginState(WebSettings.PluginState.ON);
            settings.setUseWideViewPort(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            wvDynmap.loadUrl(buildUrl());
        }
        return rootView;
    }

    private String buildUrl() {
        String url;
        if (ServerControlActivity.server.getDynmapPort().isEmpty()) {
            url = ServerControlActivity.server.getDynmapHost();
        } else {
            url = ServerControlActivity.server.getDynmapHost() + ":" + ServerControlActivity.server.getDynmapPort();
        }
        if (url.startsWith("https://")) url = url.replace("https://", "http://");
        if (!url.startsWith("http://")) url = "http://" + url;
        return url;
    }
}
