package algonquin.cst2335.curr0263;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * test to see if entering all of requirements works
     */
    @Test
    public void mainActivityTest()
    {

        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        appCompatEditText.perform(replaceText("Dave123%^&"),closeSoftKeyboard());


        ViewInteraction materialButton = onView( withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("your password meets the requirements")));
    }

    /**
     * test to see if application detects missing uppercase letter
     */
    @Test
    public void testFindMissingUpperCase()
    {

        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        appCompatEditText.perform(replaceText("password123#$*"));


        ViewInteraction materialButton = onView( withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("you shall not pass!")));
    }

    /**
     * test to see if application detects missing lowercase letter
     */
    @Test
    public void testFindMissingLowerCase()
    {

        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        appCompatEditText.perform(replaceText("PASSWORD123#$*"));


        ViewInteraction materialButton = onView( withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("you shall not pass!")));
    }

    /**
     * test to see if application detects missing number
     */
    @Test
    public void testFindMissingNumber()
    {

        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        appCompatEditText.perform(replaceText("password#$*"));


        ViewInteraction materialButton = onView( withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("you shall not pass!")));
    }

    /**
     * test to see if application detects missing special character
     */
    @Test
    public void testFindMissingSpecial()
    {

        ViewInteraction appCompatEditText = onView( withId(R.id.editText));
        appCompatEditText.perform(replaceText("passweord123"));


        ViewInteraction materialButton = onView( withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView));
        textView.check(matches(withText("you shall not pass!")));
    }



    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
