package tw.bao.languagelearner.splash.contract

import android.content.Intent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tw.bao.languagelearner.main.activity.MainActivity
import tw.bao.languagelearner.model.WordDatas
import tw.bao.languagelearner.utils.db.DBDefinetion.ASSET_FILE_EXTESION
import tw.bao.languagelearner.utils.db.DBDefinetion.WORDS_DB_NAME
import tw.bao.languagelearner.utils.db.DBDefinetion.WORD_TABLE_BASIC
import tw.bao.languagelearner.utils.db.DBDefinetion.WORD_TABLE_BUSINESS
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
            loadWordsIntoDB(WORD_TABLE_BUSINESS)
            uiThread {
                mAnswerDialogView.getContext()?.apply {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }

    private fun loadWordsIntoDB(tableName: String) {
        if (dbHelper == null) {
            mAnswerDialogView.getContext()?.apply {
                dbHelper = DBHelper(this, WORDS_DB_NAME)
            }
        }
        dbHelper?.apply {
            val dbRowCounts = getCount(tableName)
            val words: String = UtilsDB.readFromAssets(mAnswerDialogView.getContext(), tableName + ASSET_FILE_EXTESION)
            val wordDatas: WordDatas = UtilsDB.parseWordDatas(tableName, words)
            var startPostiion = 0
            if (dbRowCounts >= wordDatas.words.size) {
                return
            } else {
                startPostiion = dbRowCounts - 1
            }
            createTable(tableName)
            if (startPostiion > 0) {
                for (removePosition in 0..startPostiion) {
                    wordDatas.words.removeAt(0)
                }
            }
            insertDatas(wordDatas)
        }
    }
}