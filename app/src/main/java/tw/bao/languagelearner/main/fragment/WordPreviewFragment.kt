package tw.bao.languagelearner.main.fragment

import android.support.v4.app.Fragment
import android.util.Log

/**
 * Created by bao on 2017/12/9.
 */
class WordPreviewFragment : Fragment {
    constructor()

    companion object {
        public val sInstance: WordPreviewFragment by lazy { WordPreviewFragment() }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("FragmentVisible","WordPreviewFragment visible")

    }

}