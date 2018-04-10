package com.swiftsynq.medmanager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Created by popoolaadebimpe on 10/04/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule
            =new ActivityTestRule<MainActivity>(MainActivity.class);
}
