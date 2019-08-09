package com.example.travelmantics;

import android.util.Log;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ListActivityTest {

    @Rule
    public ActivityTestRule<ListActivity> mActivityTestRule = new ActivityTestRule<>(ListActivity.class);

@Test
        public void findDeal() {

            onView(withId(R.id.rvdeals)).perform(swipeLeft());
            try {
                Thread.sleep(9000);
            } catch (InterruptedException f) {
                Log.e("Sleep", "Sleep Interrupted");
            }

            onView(withId(R.id.imagedeals)).perform(click());

            try {
                Thread.sleep(9000);
            } catch (InterruptedException f) {
                Log.e("Sleep", "Sleep Interrupted");
            }

        }



    @Test
    public void logOut() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException f) {
            Log.e("Sleep", "Sleep Interrupted");
        }
        openActionBarOverflowOrOptionsMenu(mActivityTestRule.getActivity());


        try {
            Thread.sleep(1000);
        } catch (InterruptedException f) {
            Log.e("Sleep", "Sleep Interrupted");
        }


        onView(withText("Logout")).perform(click());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException f) {
            Log.e("Sleep", "Sleep Interrupted");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException f) {
            Log.e("Sleep", "Sleep Interrupted");
        }
        pressBack();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException f) {
            Log.e("Sleep", "Sleep Interrupted");
        }

        onView(withText("Sign in with Email")).perform(click());

        try {
            Thread.sleep(4000);
        } catch (InterruptedException f) {
            Log.e("Sleep", "Sleep Interrupted");
        }

    }}