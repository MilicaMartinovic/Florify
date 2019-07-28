package com.example.florify.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> NamePage = new ArrayList<>(); // Fragment Name List

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        NamePage.add(title);
    }
}
