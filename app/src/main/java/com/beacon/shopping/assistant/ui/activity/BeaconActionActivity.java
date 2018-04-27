

package com.beacon.shopping.assistant.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.beacon.shopping.assistant.R;
import com.beacon.shopping.assistant.model.ActionBeacon;
import com.beacon.shopping.assistant.ui.adapter.DetailFragmentPagerAdapter;
import com.beacon.shopping.assistant.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BeaconActionActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;

    private ActionBeacon mActionBeacon;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BeaconActionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        ButterKnife.bind(this);

        setupToolbar();
        readExtras();
        setupTabs();

    }

    protected void readExtras() {
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mActionBeacon = intent.getExtras().getParcelable(Constants.ARG_ACTION_BEACON);
        }
    }

    @Override
    public void finish() {
        //Constants.REQ_UPDATED_ACTION_BEACON
        int resultCode = Activity.RESULT_OK;
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.ARG_ACTION_BEACON, mActionBeacon);
        setResult(resultCode, resultIntent);
        super.finish();
    }

    private void setupTabs() {
        viewPager.setAdapter(new DetailFragmentPagerAdapter(getSupportFragmentManager(),
                mActionBeacon,
                BeaconActionActivity.this));

        slidingTabs.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
