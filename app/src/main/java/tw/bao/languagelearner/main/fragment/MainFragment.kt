package tw.bao.languagelearner.main.fragment

import android.support.v4.app.Fragment
import android.util.Log

/**
 * Created by bao on 2017/12/9.
 */

class MainFragment : Fragment {
    constructor()

    companion object {
        public val sInstance: MainFragment by lazy { MainFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("FragmentVisible","MainFragment visible")
    }

}