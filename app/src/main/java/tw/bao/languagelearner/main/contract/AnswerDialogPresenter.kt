package tw.bao.languagelearner.main.contract

/**
 * Created by bao on 2017/10/25.
 */
class AnswerDialogPresenter(view: AnswerDialogContract.View) : AnswerDialogContract.Presenter {

    var mAnswerDialogView: AnswerDialogContract.View = checkNotNull(view)

    override fun onCreate() {
        mAnswerDialogView.initView()
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {
        mAnswerDialogView.showQuestionView()
    }

    override fun onPause() {
        mAnswerDialogView.hideQuestionView()
    }
}