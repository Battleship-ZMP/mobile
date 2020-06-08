package com.example.coolrecipes


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RecipeUploadEditDeleteTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun recipeUploadEditDeleteTest() {
        val appCompatImageButton = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView.perform(click())

        val supportVectorDrawablesButton = onView(
            allOf(
                withId(R.id.email_button), withText("Sign in with email"),
                childAtPosition(
                    allOf(
                        withId(R.id.btn_holder),
                        childAtPosition(
                            withId(R.id.container),
                            0
                        )
                    ),
                    0
                )
            )
        )
        supportVectorDrawablesButton.perform(scrollTo(), click())

        val textInputEditText = onView(
            allOf(
                withId(R.id.email),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.email_layout),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText.perform(scrollTo(), replaceText("admin@mail.com"), closeSoftKeyboard())

        val appCompatButton = onView(
            allOf(
                withId(R.id.button_next), withText("Next"),
                childAtPosition(
                    allOf(
                        withId(R.id.email_top_layout),
                        childAtPosition(
                            withClassName(`is`("android.widget.ScrollView")),
                            0
                        )
                    ),
                    2
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.password),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.password_layout),
                        0
                    ),
                    0
                )
            )
        )
        textInputEditText2.perform(scrollTo(), replaceText("admin123"), closeSoftKeyboard())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.button_done), withText("Sign in"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        appCompatButton2.perform(scrollTo(), click())

        val appCompatImageButton2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val navigationMenuItemView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView2.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.recipeNameAdd),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("ESPRESSO"), closeSoftKeyboard())

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.recipeDescAdd),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatEditText2.perform(scrollTo(), replaceText("1"), closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.recipeIngredientsAdd),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatEditText3.perform(scrollTo(), replaceText("2"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.recipeMainTextAdd),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        appCompatEditText4.perform(scrollTo(), replaceText("3"), closeSoftKeyboard())

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.recipeButtonUpload), withText("Zapisz przepis"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    5
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        val appCompatImageButton3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val navigationMenuItemView3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView3.perform(click())

        val appCompatRadioButton = onView(
            allOf(
                withId(R.id.sortByDate), withText("Data dodania"),
                childAtPosition(
                    allOf(
                        withId(R.id.sortModeButtonGroup),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatRadioButton.perform(click())

        val cardView = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recyclerview_main_list),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        val appCompatButton4 = onView(
            allOf(
                withId(R.id.editButton), withText("edytuj przepis"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    12
                )
            )
        )
        appCompatButton4.perform(scrollTo(), click())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.recipeNameAdd), withText("ESPRESSO"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        appCompatEditText5.perform(scrollTo(), replaceText("ESPRESSO 2"))

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.recipeNameAdd), withText("ESPRESSO 2"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(closeSoftKeyboard())

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.recipeDescAdd), withText("1"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatEditText7.perform(scrollTo(), replaceText("1 3"))

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.recipeDescAdd), withText("1 3"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(closeSoftKeyboard())

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.recipeIngredientsAdd), withText("2"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        )
        appCompatEditText9.perform(scrollTo(), replaceText("2 4"))

        val appCompatEditText10 = onView(
            allOf(
                withId(R.id.recipeIngredientsAdd), withText("2 4"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        appCompatEditText10.perform(closeSoftKeyboard())

        val appCompatEditText11 = onView(
            allOf(
                withId(R.id.recipeMainTextAdd), withText("3"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        )
        appCompatEditText11.perform(scrollTo(), replaceText("3 5"))

        val appCompatEditText12 = onView(
            allOf(
                withId(R.id.recipeMainTextAdd), withText("3 5"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatEditText12.perform(closeSoftKeyboard())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.recipeButtonUpload), withText("Zapisz przepis"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    5
                )
            )
        )
        appCompatButton5.perform(scrollTo(), click())

        val appCompatImageButton4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val navigationMenuItemView4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView4.perform(click())

        val appCompatImageButton5 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton5.perform(click())

        val navigationMenuItemView5 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView5.perform(click())

        val appCompatRadioButton2 = onView(
            allOf(
                withId(R.id.sortByDate), withText("Data dodania"),
                childAtPosition(
                    allOf(
                        withId(R.id.sortModeButtonGroup),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatRadioButton2.perform(click())

        val cardView2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recyclerview_main_list),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView2.perform(click())

        val appCompatButton6 = onView(
            allOf(
                withId(R.id.deleteButton), withText("usuń przepis"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    13
                )
            )
        )
        appCompatButton6.perform(scrollTo(), click())

        val appCompatImageButton6 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageButton6.perform(click())

        val navigationMenuItemView6 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.design_navigation_view),
                        childAtPosition(
                            withId(R.id.nav_view),
                            0
                        )
                    ),
                    10
                ),
                isDisplayed()
            )
        )
        navigationMenuItemView6.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

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
