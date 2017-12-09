package tw.bao.languagelearner.main.fragment

import android.support.v4.app.Fragment
import android.util.Log

/**
 * Created by bao on 2017/12/9.
 */

class SettingFragment : Fragment {
    constructor()


    companion object {
        public val sInstance: SettingFragment by lazy { SettingFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        Log.d("FragmentVisible","SettingFragment visible")
    }

}