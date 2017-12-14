package tw.bao.languagelearner.main

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tw.bao.languagelearner.R
import tw.bao.languagelearner.info.fragment.InfoFragment
import tw.bao.languagelearner.setting.fragment.SettingFragment
import tw.bao.languagelearner.wordspreview.fragment.WordPreviewFragment

/**
 * Created by bao on 2017/12/9.
 */
class MainLayoutHelper {
    companion object {

        public enum class PageEnum {
            MAIN, WORD_PREVIEW, SETTING
        }

        private val sPageConfigs: ArrayList<PageConfig?> by lazy { arrayListOf(getPageConfig(PageEnum.MAIN), getPageConfig(PageEnum.WORD_PREVIEW), getPageConfig(PageEnum.SETTING)) }

        fun initTabIcons(context: Context, tabLayout: TabLayout) {
            Log.d("TABINIT", "config size : " + sPageConfigs.size)
            for (config in sPageConfigs) {
                Log.d("TABINIT", "Add Tab")
                config?.apply {
                    val customView = inflateCustomTab(context, mTabUnselectedIconResId, mTitleStrId)
                    val newTab = tabLayout.newTab().setCustomView(customView)
                    val view = customView.parent as View
                    view.tag = mPageEnum
                    tabLayout.addTab(newTab)
                    Log.d("TABINIT", "Add Tab")
                }
            }
        }

        private fun inflateCustomTab(context: Context, iconResId: Int, titleResId: Int): View {
            val view = LayoutInflater.from(context).inflate(R.layout.bottom_custom_tab, null)
            val tabImage: ImageView = view.findViewById(R.id.iv_tab_icon)
            val tabText: TextView = view.findViewById(R.id.tv_tab_text)
            tabImage.setImageResource(iconResId)
            tabText.setText(titleResId)
            return view
        }

        private fun getPageConfig(page: PageEnum): PageConfig? {
            return when (page) {
                PageEnum.MAIN -> PageConfig(
                        PageEnum.MAIN,
                        InfoFragment::class.java,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher,
                        R.string.info_tab_name
                )
                PageEnum.WORD_PREVIEW -> PageConfig(
                        PageEnum.WORD_PREVIEW,
                        WordPreviewFragment::class.java,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher,
                        R.string.word_preview_tab_name
                )
                PageEnum.SETTING -> PageConfig(
                        PageEnum.SETTING,
                        SettingFragment::class.java,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher,
                        R.string.setting_tab_name
                )
            }
        }

        fun getPageFragment(page: PageEnum): Fragment? {
            return when (page) {
                PageEnum.MAIN -> InfoFragment.sInstance
                PageEnum.WORD_PREVIEW -> WordPreviewFragment.sInstance
                PageEnum.SETTING -> SettingFragment.sInstance
            }
        }


        fun getPageIndex(pageEnum: Enum<*>): Int {
            return (0 until sPageConfigs.size).firstOrNull { sPageConfigs[it]?.mPageEnum == pageEnum }
                    ?: 0
        }
    }
}