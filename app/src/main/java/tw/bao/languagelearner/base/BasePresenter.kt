package tw.bao.languagelearner.base

/**
 * Created by bao on 2017/10/25.
 */
interface BasePresenter {
    fun onCreate()
    fun onStart()
    fun onStop()
    fun onResume()
    fun onPause()
}