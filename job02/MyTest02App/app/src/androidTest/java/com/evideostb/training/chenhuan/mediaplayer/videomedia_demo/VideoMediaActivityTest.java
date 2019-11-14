package com.evideostb.training.chenhuan.mediaplayer.videomedia_demo;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.evideostb.training.chenhuan.mediaplayer.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by ChenHuan on 2018/3/12.
 */
@MediumTest
@RunWith(AndroidJUnit4.class)
public class VideoMediaActivityTest {
    @Rule
    public ActivityTestRule<VideoMediaActivity> mActivityRule = new ActivityTestRule<>(VideoMediaActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void onCreate() throws Exception {
    }

    @Test
    public void onClick() throws Exception {
        onView(withId(R.id.bt_play_pause)).perform(click());
    }

    @Test
    public void onDestroy() throws Exception {
    }

}
