package tw.bao.languagelearner.splash.contract

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
}