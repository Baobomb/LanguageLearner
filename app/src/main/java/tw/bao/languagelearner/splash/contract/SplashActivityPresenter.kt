package tw.bao.languagelearner.splash.contract

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.db.DBDefinetion.ASSET_FILE_EXTESION
import tw.bao.languagelearner.utils.db.DBDefinetion.WORDS_DB_NAME
import tw.bao.languagelearner.utils.db.DBDefinetion.WORD_TABLE_BASIC
import tw.bao.languagelearner.utils.db.DBHelper
import tw.bao.languagelearner.utils.db.UtilsDB

/**
 * Created by bao on 2017/10/25.
 */
class SplashActivityPresenter(view: SplashActivityContract.View) : SplashActivityContract.Presenter {

    var mAnswerDialogView: SplashActivityContract.View = checkNotNull(view)

    var dbHelper: DBHelper? = null

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
            loadWordsIntoDB(WORD_TABLE_BASIC)
            uiThread {
            }
        }
    }

    private fun loadWordsIntoDB(tableName: String) {
        if (dbHelper == null) {
            dbHelper = DBHelper(mAnswerDialogView.getContext(), WORDS_DB_NAME, 1)
        }
        val words: String = UtilsDB.readFromAssets(mAnswerDialogView.getContext(), tableName + ASSET_FILE_EXTESION)
        val wordDatas: WordDatas = UtilsDB.parseWordDatas(WORD_TABLE_BASIC, words)
        dbHelper?.apply {
            createTable(tableName)
            insertDatas(wordDatas)
        }
    }
}