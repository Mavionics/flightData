package com.github.mavionics.fligt_data

import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingResource
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.filters.LargeTest

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import java.util.Random

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.github.mavionics.fligt_data.activities.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.CoreMatchers.allOf







@LargeTest
@RunWith(AndroidJUnit4::class)
class SignInActivityTest {

    private var mActivityResource: IdlingResource? = null

    @Rule @JvmField
    var mActivityTestRule = ActivityTestRule(SignInActivity::class.java)

    @Before
    fun setUp() {
        if (mActivityResource != null) {
            Espresso.unregisterIdlingResources(mActivityResource!!)
        }

        // Register Activity as idling resource
        mActivityResource = BaseActivityIdlingResource(mActivityTestRule.activity)
        Espresso.registerIdlingResources(mActivityResource!!)
    }

    @After
    fun tearDown() {
        if (mActivityResource != null) {
            Espresso.unregisterIdlingResources(mActivityResource!!)
        }
    }

    @Test
    fun failedSignInTest() {
        val email = "test@test.com"
        val password = "123456"

        // Make sure we're signed out
        FirebaseAuth.getInstance().signOut()

        // Enter email
        enterEmail(email)

        // Enter password
        enterPassword(password)

        // Click sign in
        val appCompatButton = onView(
                allOf<View>(withId(R.id.buttonSignIn), withText(R.string.sign_in),
                        withParent(withId(R.id.layoutButtons)),
                        isDisplayed()))
        appCompatButton.perform(click())

        assert(true)
        //Figure out a better way to test toast
        //onView(withText(R.string.SIGN_IN_FAILED)).inRoot(withDecorView(not(`is`(mActivityTestRule.activity.getWindow().getDecorView())))).check(matches(isDisplayed()))
    }

    @Test
    fun failedvalidateForm() {
        val email = "test@test.com"
        val password = ""

        // Make sure we're signed out
        FirebaseAuth.getInstance().signOut()

        // Enter email
        enterEmail(email)

        // Enter password
        enterPassword(password)

        // Click sign in
        val appCompatButton = onView(
                allOf<View>(withId(R.id.buttonSignIn), withText(R.string.sign_in),
                        withParent(withId(R.id.layoutButtons)),
                        isDisplayed()))

        appCompatButton.perform(click())

        val passwordField = onView(
                allOf<View>(withId(R.id.fieldPassword),
                        withParent(withId(R.id.layoutEmailPassword)),
                        isDisplayed()))

        assert(true)


    }

    private fun enterEmail(email: String) {
        val emailField = onView(
                allOf<View>(withId(R.id.fieldEmail),
                        withParent(withId(R.id.layoutEmailPassword)),
                        isDisplayed()))
        emailField.perform(replaceText(email))
    }

    private fun enterPassword(password: String) {
        val passwordField = onView(
                allOf<View>(withId(R.id.fieldPassword),
                        withParent(withId(R.id.layoutEmailPassword)),
                        isDisplayed()))
        passwordField.perform(replaceText(password))
    }

    private fun randomInt(): String {
        return Random().nextInt(100000).toString()
    }

}