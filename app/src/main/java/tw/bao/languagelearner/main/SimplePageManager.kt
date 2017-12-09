package tw.bao.languagelearner.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import tw.bao.languagelearner.R

/**
 * Created by bao on 2017/12/9.
 */

class SimplePageManager(
        val manager: FragmentManager,
        val adapter: Adapter,
        val callback: FragmentCallback) {

    companion object {
        const val UNKNOWN_INDEX = -1
    }

    //Last page
    var lastPageIndex = UNKNOWN_INDEX

    //Current page
    var currentPage = MainLayoutHelper.Companion.PageEnum.MAIN
    var currentPageIndex = 0

    fun select(index: Int, page: MainLayoutHelper.Companion.PageEnum) {
        //Save last
        lastPageIndex = currentPageIndex

        //Assign new
        currentPage = page
        currentPageIndex = index
    }

    fun attachFragment() {

        //Simulate the last fragment is not visible anymore
        val lastFragment = getLastFragment()
        lastFragment?.userVisibleHint = false

        var selectedFragment = getCurrentFragment()

        if (selectedFragment != null) {
            //Cache hit, use cached fragment

            invokeOnDetach()

            val fragmentTransaction = manager.beginTransaction()

            //Detach all attached fragments
            val list = manager.fragments
            for (fragment in list) {
                fragmentTransaction.detach(fragment)
            }

            fragmentTransaction
                    .attach(selectedFragment)
                    .commit()

            invokeOnAttach()

            //Simulate the selected fragment is visible now
            selectedFragment.userVisibleHint = true

        } else {
            //No available fragment for this ID, create a new one and put back to cache
            invokeOnDetach()

            selectedFragment = adapter.createFragment(currentPageIndex)

            val fragmentTransaction = manager.beginTransaction()

            //Detach all attached fragments
            val list = manager.fragments
            for (fragment in list) {
                fragmentTransaction.detach(fragment)
            }

            fragmentTransaction
                    .add(R.id.fragment_container, selectedFragment, adapter.getFragmentTag(currentPageIndex))
                    .commit()

            invokeOnAttach()

            //Simulate the selected fragment is visible now
            selectedFragment.userVisibleHint = true
        }
    }

    private fun invokeOnAttach() {
        callback.onAttach(currentPageIndex)
    }

    private fun invokeOnDetach() {
        callback.onDetach(lastPageIndex)
    }

    fun getCurrentFragment(): Fragment? {
        return getFragmentByIndex(currentPageIndex)
    }

    private fun getLastFragment(): Fragment? {
        return takeIf { lastPageIndex >= 0 }?.getFragmentByIndex(lastPageIndex)
    }

    private fun getFragmentByIndex(index: Int): Fragment? {
        return manager.findFragmentByTag(adapter.getFragmentTag(index))
    }

    interface Adapter {
        fun createFragment(index: Int): Fragment
        fun getFragmentTag(index: Int): String
    }

    interface FragmentCallback {
        fun onAttach(index: Int)
        fun onDetach(index: Int)
    }
}

