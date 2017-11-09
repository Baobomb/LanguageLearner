package tw.bao.languagelearner.splash.contract

import tw.bao.languagelearner.utils.db.DBDefinetion
import java.io.File

/**
 * Created by bao on 2017/10/25.
 */
class SplashActivityPresenter(view: SplashActivityContract.View) : SplashActivityContract.Presenter {

    var mAnswerDialogView: SplashActivityContract.View = checkNotNull(view)

    override fun onCreate() {
        mAnswerDialogView.initView()
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun initDB() {
        val words = ""
        Word
    }
}