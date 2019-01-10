package com.max.wechatluckymoney.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.max.wechatluckymoney.R;
import com.max.wechatluckymoney.activitys.WebViewActivity;
import com.max.wechatluckymoney.services.HandlerHelper;
import com.max.wechatluckymoney.utils.Utils;

/**
 * Created by Max on 2018/2/10.
 * 设置
 */
public class GeneralSettingsFragment extends PreferenceFragment {

    public static GeneralSettingsFragment getInstance() {
        GeneralSettingsFragment fragment = new GeneralSettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_preferences);
        initEvent();
    }

    private void initEvent() {
        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(preference -> {

            Intent intent = WebViewActivity.getInstance(getActivity(), getString(R.string.str_github_issues),
                    getString(R.string.url_github_issues));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return false;
        });


        Preference excludeWordsPref = findPreference("pref_watch_exclude_words");
        String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
        String value = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_watch_exclude_words", "");
        if (value.length() > 0) excludeWordsPref.setSummary(summary + ":" + value);

        excludeWordsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
                if (o != null && o.toString().length() > 0) {
                    preference.setSummary(summary + ":" + o.toString());
                } else {
                    preference.setSummary(summary);
                }
                return true;
            }
        });


        Preference excludeWordsPrefChat = findPreference("pref_watch_exclude_words_chat");
        String summaryChat = getResources().getString(R.string.pref_watch_exclude_words_summary);
        String valueChat = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_watch_exclude_words_chat", "");
        if (valueChat.length() > 0) excludeWordsPrefChat.setSummary(summaryChat + ":" + valueChat);

        excludeWordsPrefChat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
                if (o != null && o.toString().length() > 0) {
                    preference.setSummary(summary + ":" + o.toString());
                } else {
                    preference.setSummary(summary);
                }
                return true;
            }
        });


        Preference compatibilityPref = findPreference("pref_etc_compatibility");
        String[] arr = HandlerHelper.getAdapterVersion();
        String wechatVersion = Utils.getWeChatVersion(getActivity());
        if (Utils.isArrContains(arr, Utils.getWeChatVersion(getActivity()))) {
            compatibilityPref.setSummary("当前微信" + wechatVersion + "版本已适配,但部分手机可能无法正常运转^_^\\\"");
        } else {
            compatibilityPref.setSummary("未适配当前微信" + wechatVersion + "版本,敬请期待^_^\\\"");
        }

        compatibilityPref.setOnPreferenceClickListener(preference -> {
            if (Utils.isArrContains(arr, Utils.getWeChatVersion(getActivity()))) {
                Toast.makeText(getActivity(), "当前微信版本已适配", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "未适配当前微信版本,敬请期待~", Toast.LENGTH_LONG).show();
            }
            return false;
        });
    }
}
