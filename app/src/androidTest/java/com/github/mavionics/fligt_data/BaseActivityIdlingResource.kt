package com.github.mavionics.fligt_data

import android.support.test.espresso.IdlingResource

import com.github.mavionics.fligt_data.activities.BaseActivity
import com.github.mavionics.fligt_data.activities.SignInActivity

/**
 * Monitor Activity idle status by watching ProgressDialog.
 */
class BaseActivityIdlingResource : IdlingResource {

    private var mActivity: BaseActivity? = null
    private var mCallback: IdlingResource.ResourceCallback? = null

    constructor(activity: SignInActivity) {
        mActivity = activity
    }

    override fun getName(): String {
        return "BaseActivityIdlingResource:" + mActivity!!.localClassName
    }

    override fun isIdleNow(): Boolean {
        val dialog = mActivity!!.mProgressDialog
        val idle = dialog == null || !dialog!!.isShowing()

        if (mCallback != null && idle) {
            mCallback!!.onTransitionToIdle()
        }

        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        mCallback = callback
    }
}