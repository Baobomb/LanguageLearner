package tw.bao.languagelearner.main.contract

import android.content.Intent
import tw.bao.languagelearner.main.activity.WordsPreviewActivity

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

    public fun startWordsPreview(tableName: String) {
        mMainActivityView.getContext()?.apply {
            val intent = Intent(this, WordsPreviewActivity::class.java)
            intent.putExtra(WordsPreviewActivityPresenter.KEY_WORD_PREVIEW_TABLE_NAME, tableName)
            startActivity(intent)
        }
    }
}