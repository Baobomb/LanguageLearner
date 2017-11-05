package tw.bao.languagelearner.main.contract

/**
 * Created by bao on 2017/10/25.
 */
class MainActivityPresenter(view: MainActivityContract.View) : MainActivityContract.Presenter {

    var mMainActivityView: MainActivityContract.View = checkNotNull(view)

    override fun onCreate() {
        mMainActivityView.initView()
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