package tw.bao.languagelearner.splash.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import tw.bao.languagelearner.R
import tw.bao.languagelearner.splash.contract.SplashActivityContract
import tw.bao.languagelearner.splash.contract.SplashActivityPresenter

/**
 * Created by bao on 2017/10/27.
 */
class SplashActivity : AppCompatActivity(), SplashActivityContract.View {
    lateinit var mPresenter: SplashActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = SplashActivityPresenter(this)
        mPresenter.onCreate()
    }

    override fun initView() {
        setContentView(R.layout.activity_splash_layout)
    }
}