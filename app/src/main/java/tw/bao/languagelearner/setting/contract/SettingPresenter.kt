package tw.bao.languagelearner.main.contract

/**
 * Created by bao on 2017/10/25.
 */
class SettingPresenter(view: SettingContract.View) : SettingContract.Presenter {

    var mSettingView: SettingContract.View = checkNotNull(view)

    override fun onCreate() {

    }

    fun onCreateView() {
    }

    fun onViewCreated() {
        mSettingView.initView()
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