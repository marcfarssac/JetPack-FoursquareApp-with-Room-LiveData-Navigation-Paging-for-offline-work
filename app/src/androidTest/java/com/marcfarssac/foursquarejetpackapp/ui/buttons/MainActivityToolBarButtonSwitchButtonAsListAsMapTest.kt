package com.marcfarssac.foursquarejetpackapp.ui.buttons


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.marcfarssac.foursquarejetpackapp.MainActivity
import com.marcfarssac.foursquarejetpackapp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityToolBarButtonSwitchButtonAsListAsMapTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityToolBarButtonSwitchButtonAsListAsMapTest() {
        val textView = onView(
                allOf(withId(R.id.option_show_venues_format), withText("AS LIST"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                1),
                        isDisplayed()))
        textView.check(matches(withText("AS LIST")))

        val actionMenuItemView = onView(
                allOf(withId(R.id.option_show_venues_format), withText("AS LIST"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                1),
                        isDisplayed()))
        actionMenuItemView.perform(click())

        val textView2 = onView(
                allOf(withId(R.id.option_show_venues_format), withText("ON MAP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                1),
                        isDisplayed()))
        textView2.check(matches(withText("ON MAP")))

        val actionMenuItemView2 = onView(
                allOf(withId(R.id.option_show_venues_format), withText("ON MAP"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                1),
                        isDisplayed()))
        actionMenuItemView2.perform(click())

        val textView3 = onView(
                allOf(withId(R.id.option_show_venues_format), withText("AS LIST"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        2),
                                1),
                        isDisplayed()))
        textView3.check(matches(withText("AS LIST")))
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
