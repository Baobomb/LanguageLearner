package tw.bao.languagelearner.wordspreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.view_item_word_datas_list.view.*
import tw.bao.languagelearner.R
import tw.bao.languagelearner.main.contract.WordsPreviewContract
import tw.bao.languagelearner.model.WordDatas

/**
 * Created by bao on 2017/11/29.
 */
class WordDatasListAdapter(context: Context) : RecyclerView.Adapter<WordDatasListAdapter.WordDatasListItemViewHolder>() {

    var mContext: Context? = context
    var mWordDatas: WordDatas? = null
        set(wordDatas) {
            field = wordDatas
            notifyDataSetChanged()
        }
    public var onItemClickListener: WordsPreviewContract.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WordDatasListItemViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.view_item_word_datas_list, parent, false)
        return WordDatasListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordDatasListItemViewHolder?, position: Int) {
        mWordDatas?.words?.takeIf { it[position] != null }?.apply {
            holder?.itemView?.mTvWordEng?.text = this[position]?.engWord
            holder?.itemView?.mTvWordChinese?.text = this[position]?.chineseWord
            holder?.itemView?.mTvWordRoman?.text = this[position]?.romanText
            holder?.itemView?.mTvWordEng?.setOnClickListener {
                onItemClickListener?.onEngWordClick((it as TextView).text.toString())
            }
            holder?.itemView?.mTvWordChinese?.setOnClickListener {
                onItemClickListener?.onChineseWordClick((it as TextView).text.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        mWordDatas?.apply {
            return words.size
        }
        return 0
    }

    class WordDatasListItemViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView)
}