package cz.boosik.boosadminforminecraft.app.presenter.fragments;

import android.os.Bundle;
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
import cz.boosik.boosadminforminecraft.app.presenter.activities.ServerControlActivity;

/**
 * Fragment used to display dynamic map
 *
 * @author jakub.kolar@bsc-ideas.com
 */
public class ServerControlDynmapFragment extends AbstractServerControlFragment {

    @Bind(R.id.wvDynmap)
    WebView wvDynmap;

    static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server_control_dynmap, container, false);
        ButterKnife.bind(this, rootView);
        setupWebView();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        wvDynmap.onPause();
        wvDynmap.pauseTimers();
    }

    @Override
    public void onResume() {
        super.onResume();
        wvDynmap.onResume();
        wvDynmap.resumeTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wvDynmap.loadUrl("about:blank");
        wvDynmap.stopLoading();
        wvDynmap.setWebChromeClient(null);
        wvDynmap.setWebViewClient(null);
        wvDynmap.destroy();
        wvDynmap = null;
    }

    /**
     * Creates new instance of this fragment with the section number used for paging
     *
     * @param sectionNumber Section number in pager
     * @return Instance of this fragment with set section number
     */
    public static ServerControlDynmapFragment newInstance(int sectionNumber) {
        ServerControlDynmapFragment fragment = new ServerControlDynmapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Builds the dynamic map URL to the desired format
     *
     * @return formatted URL
     */
    private String buildUrl() {
        String url;
        if (selectedServer.getDynmapPort().isEmpty()) {
            url = selectedServer.getDynmapHost();
        } else {
            url = selectedServer.getDynmapHost() + ":" + selectedServer.getDynmapPort();
        }
        if (url.startsWith("https://")) url = url.replace("https://", "http://");
        if (!url.startsWith("http://")) url = "http://" + url;
        return url;
    }

    /**
     * Set up the webview
     */
    private void setupWebView() {
        if (((ServerControlActivity) getActivity()).isDynmapAvailable()) {
            wvDynmap.setWebChromeClient(new WebChromeClient());
            wvDynmap.setWebViewClient(new WebViewClient());
            WebSettings settings = wvDynmap.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            wvDynmap.loadUrl(buildUrl());
            wvDynmap.requestFocus();
        }
    }
}
