package tw.bao.languagelearner.main.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main_layout.*
import kotlinx.android.synthetic.main.app_bar_main.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.MainLayoutHelper
import tw.bao.languagelearner.main.contract.MainActivityContract
import tw.bao.languagelearner.main.contract.MainActivityPresenter

/**
 * Created by bao on 2017/10/25.
 */
class MainActivity : AppCompatActivity(), MainActivityContract.View {

    lateinit var mPresenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = MainActivityPresenter(this)
        mPresenter.onCreate()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.onStop()
    }

    override fun initView() {
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
    }

    override fun getContext(): Context? = this

    private val mTabOnClickListener = View.OnClickListener { view ->
        val selectedPage = view.tag as MainLayoutHelper.Companion.PageEnum
        selectPage(selectedPage)
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

    private fun selectPage(pageEnum: MainLayoutHelper.Companion.PageEnum) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        //Detach all attached fragments
        val list = supportFragmentManager.fragments
        for (fragment in list) {
            fragmentTransaction.detach(fragment)
        }

        val selectFragment = MainLayoutHelper.getPageFragment(pageEnum)
        selectFragment?.apply {
            fragmentTransaction
                    .attach(this)
                    .commit()
            userVisibleHint = true
        }
    }

}