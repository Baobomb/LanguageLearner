package tw.bao.languagelearner.splash.contract

import android.util.Log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.utils.db.DBDefinetion
import tw.bao.languagelearner.utils.db.DBHelper
import tw.bao.languagelearner.utils.db.UtilsDB

/**
 * Created by bao on 2017/10/25.
 */
class SplashActivityPresenter(view: SplashActivityContract.View) : SplashActivityContract.Presenter {

    var mAnswerDialogView: SplashActivityContract.View = checkNotNull(view)

    override fun onCreate() {
        mAnswerDialogView.initView()
    }

    override fun onStart() {
        initDB()
    }

    override fun onStop() {

    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun initDB() {
        doAsync {
            val words = UtilsDB.readFromAssets(mAnswerDialogView.getContext(), DBDefinetion.WORD_TABLE_BASIC + DBDefinetion.ASSET_FILE_EXTESION)
            val dbHelper = DBHelper(mAnswerDialogView.getContext(), DBDefinetion.WORD_TABLE_BASIC, 1)
            dbHelper.createTable(DBDefinetion.WORD_TABLE_BASIC)
            val wordDatas = UtilsDB.parseWordDatas(DBDefinetion.WORD_TABLE_BASIC, words)
            dbHelper.insertDatas(wordDatas)
            val count = dbHelper.getCount(DBDefinetion.WORD_TABLE_BASIC)
            uiThread {
                Log.d("Words", " Words : " + count)
                Log.d("Words", " Words : " + words)
            }
//            mAnswerDialogView.getContext().startActivity{}
        }
    }
}