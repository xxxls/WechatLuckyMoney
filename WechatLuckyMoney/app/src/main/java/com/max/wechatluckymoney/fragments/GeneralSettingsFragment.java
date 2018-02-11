package com.max.wechatluckymoney.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.max.wechatluckymoney.R;

/**
 * Created by Max on 2018/2/10.
 * 设置
 */
public class GeneralSettingsFragment extends PreferenceFragment
{

    public static GeneralSettingsFragment getInstance()
    {
        GeneralSettingsFragment fragment = new GeneralSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_preferences);
    }

    private void setPrefListeners()
    {

        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
//                Intent webViewIntent = new Intent(getActivity(), WebViewActivity.class);
//                webViewIntent.putExtra("title", "GitHub Issues");
//                webViewIntent.putExtra("url", getString(R.string.url_github_issues));
//                webViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(webViewIntent);
                return false;
            }
        });
    }
}
