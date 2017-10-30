package tw.bao.languagelearner.main.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tw.bao.languagelearner.R
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
    }
}