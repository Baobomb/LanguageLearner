package tw.bao.languagelearner.main.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main_layout.*
import kotlinx.android.synthetic.main.app_bar_main.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.MainLayoutHelper

/**
 * Created by bao on 2017/10/25.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        val LOG_TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onBackPressed() {
        if (mMainDrawerLayout.isDrawerOpen(mNav_view)) {
            mMainDrawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    fun initView() {
        setContentView(R.layout.activity_main_layout)
        mMainToolbar.title = ""
        setSupportActionBar(mMainToolbar)

        val toggle = ActionBarDrawerToggle(
                this,
                mMainDrawerLayout,
                mMainToolbar,
                0,
                0
        )
        mMainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        MainLayoutHelper.initTabIcons(this, mMainBottomTab)
        setBottomTabListener()
        selectPage(MainLayoutHelper.getPageIndex(MainLayoutHelper.Companion.PageEnum.MAIN), MainLayoutHelper.Companion.PageEnum.MAIN)
    }

    private val mTabOnClickListener = View.OnClickListener { view ->
        val selectedPage = view.tag as MainLayoutHelper.Companion.PageEnum
        selectPage(MainLayoutHelper.getPageIndex(selectedPage), selectedPage)
    }


    private fun setBottomTabListener() {
        for (tabIndex in 0 until mMainBottomTab.tabCount) {
            val tab = mMainBottomTab.getTabAt(tabIndex)
            var customView: View?
            var parent: View?
            tab?.apply {
                customView = tab.customView
                if (customView != null) {
                    parent = customView!!.parent as View
                    parent?.setOnClickListener(mTabOnClickListener)
                }
            }
        }
    }


    var currentPage = MainLayoutHelper.Companion.PageEnum.MAIN
    var currentPageIndex = -1
    var lastPageIndex = -1

    private fun selectPage(index: Int, pageEnum: MainLayoutHelper.Companion.PageEnum) {
        Log.d(LOG_TAG, "selectPage > index : $index page ${pageEnum.name}")
        if (currentPageIndex == index) {
            Log.d(LOG_TAG, "currentPageIndex == index do not need transition")
            return
        }
        lastPageIndex = currentPageIndex

        //Assign new
        currentPage = pageEnum
        currentPageIndex = index
        val lastFragment = getLastFragment()
        lastFragment?.userVisibleHint = false

        //Detach all attached fragments

        var selectedFragment = getFragmentByIndex(index)
        if (selectedFragment != null) {
            Log.d(LOG_TAG, "selectPage > selectedFragment !=null : $selectedFragment")
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val list = supportFragmentManager.fragments
            for (fragment in list) {
                fragmentTransaction.detach(fragment)
            }
            fragmentTransaction
                    .attach(selectedFragment)
                    .commit()
            selectedFragment.userVisibleHint = true
            return
        }

        selectedFragment = MainLayoutHelper.getPageFragment(pageEnum)
        selectedFragment?.apply {
            Log.d(LOG_TAG, "selectPage > selectedFragment == null : $selectedFragment")
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val list = supportFragmentManager.fragments
            for (fragment in list) {
                fragmentTransaction.detach(fragment)
            }
            fragmentTransaction
                    .add(R.id.fragment_container, this, getFragmentTag(currentPageIndex))
                    .commit()
            //Simulate the selected fragment is visible now
            userVisibleHint = true
        }

    }


    private fun getFragmentByIndex(index: Int): Fragment? {
        return supportFragmentManager.findFragmentByTag(getFragmentTag(index))
    }

    private fun getFragmentTag(index: Int): String {
        return "android:switcher:" + index
    }

    private fun getLastFragment(): Fragment? {
        return takeIf { lastPageIndex >= 0 }?.getFragmentByIndex(lastPageIndex)
    }

}