package tw.bao.languagelearner.main

import android.support.v4.app.Fragment

/**
 * Created by bao on 2017/12/9.
 */

class PageConfig constructor(internal var mPageEnum: MainLayoutHelper.Companion.PageEnum, internal var mFragmentClass: Class<*>, internal var mTabSelectedIconResId: Int, internal var mTabUnselectedIconResId: Int, internal var mTitleStrId: Int = 0) {

    internal var mIconFontResId: Int = 0

    internal fun createFragmentInstance(): Fragment {
        // customized instance creator also goes here
        try {
            return mFragmentClass.getConstructor().newInstance() as Fragment
        } catch (e: Exception) {
            throw IllegalStateException("Cannot create object")
        }
    }
}