package org.wikiedufoundation.wikiedudashboard.course_detail.view;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.wikiedufoundation.wikiedudashboard.R;
import org.wikiedufoundation.wikiedudashboard.course_detail.articles_edited.view.CourseArticlesEditedFragment;
import org.wikiedufoundation.wikiedudashboard.campaign.view.CampaignListFragment;
import org.wikiedufoundation.wikiedudashboard.course_detail.data.CourseDetail;
import org.wikiedufoundation.wikiedudashboard.course_detail.presenter.CourseDetailPresenter;
import org.wikiedufoundation.wikiedudashboard.course_detail.presenter.CourseDetailPresenterImpl;
import org.wikiedufoundation.wikiedudashboard.course_detail.provider.RetrofitCourseDetailProvider;
import org.wikiedufoundation.wikiedudashboard.helper.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseDetailActivity extends AppCompatActivity implements CourseDetailView {

    private CourseDetailPresenter courseDetailPresenter;
    private String url;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ViewPagerAdapter viewPagerAdapter;
    private CourseHomeFragment courseHomeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        url = getIntent().getStringExtra("url");
        ButterKnife.bind(this);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        courseDetailPresenter = new CourseDetailPresenterImpl(this, new RetrofitCourseDetailProvider());
        courseDetailPresenter.requestCourseDetail(url);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(CourseDetail data) {
        toolbar.setTitle(data.getTitle());
        courseHomeFragment = CourseHomeFragment.newInstance(data);
        setTabs();
    }

    private void setTabs() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        titleList.add("Home");
        fragmentList.add(courseHomeFragment);
        titleList.add("Timeline");
        fragmentList.add(new CampaignListFragment());
        titleList.add("Students");
        fragmentList.add(new CampaignListFragment());
        titleList.add("Articles");
        CourseArticlesEditedFragment courseArticlesEditedFragment=new CourseArticlesEditedFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        courseArticlesEditedFragment.setArguments(bundle);
        fragmentList.add(courseArticlesEditedFragment);
        titleList.add("Uploads");
        fragmentList.add(new CampaignListFragment());
        titleList.add("Activity");
        fragmentList.add(new CampaignListFragment());
        viewPagerAdapter.setTabData(fragmentList, titleList);
        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show){
            progressBar.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
