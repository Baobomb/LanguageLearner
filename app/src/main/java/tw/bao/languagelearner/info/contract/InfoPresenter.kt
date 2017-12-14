package tw.bao.languagelearner.info.contract

/**
 * Created by bao on 2017/10/25.
 */
class InfoPresenter(view: InfoContract.View) : InfoContract.Presenter {

    var mInfoView: InfoContract.View = checkNotNull(view)

    override fun onCreate() {
        mInfoView.initView()
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