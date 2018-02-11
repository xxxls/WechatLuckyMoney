package com.max.wechatluckymoney.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.activitys.WebViewActivity;

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
        initEvent();
    }

    private void initEvent()
    {
        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {

                Intent intent = WebViewActivity.getInstance(getActivity(), getString(R.string.str_github_issues),
                        getString(R.string.url_github_issues));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        });
    }
}
