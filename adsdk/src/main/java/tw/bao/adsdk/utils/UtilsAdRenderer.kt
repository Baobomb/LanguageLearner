package tw.bao.adsdk.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * Created by carlosyang on 2017/10/3.
 */

object UtilsAdRenderer {


    @JvmStatic
    fun loadAdPhoto(imageUrl: String, imgView: ImageView, isSupportGif: Boolean) {
        loadAdPhoto(imageUrl, imgView, isSupportGif, 0, RoundedCornersTransformation.CornerType.ALL)
    }

    @JvmStatic
    fun loadAdPhoto(imageUri: Uri, imgView: ImageView, isSupportGif: Boolean) {
        loadAdPhoto(imageUri, imgView, isSupportGif, 0, RoundedCornersTransformation.CornerType.ALL)
    }

    @JvmStatic
    fun loadAdPhoto(imageSource: Any?, imgView: ImageView?, isSupportGif: Boolean, cornerRadiusInDp: Int, cornerType: RoundedCornersTransformation.CornerType) {
        var imageUrl: String? = null
        var imageUri: Uri? = null
        if (imageSource is Uri) {
            imageUri = imageSource
        } else if (imageSource is String) {
            imageUrl = imageSource.toLowerCase()
        }
        if ((imageUrl.isNullOrEmpty() && imageUri == null) || imgView == null) {
            return
        }

        val context = imgView.context
        if (!AdUtils.checkActivityAlive(context)) {
            return
        }
        val cornersTransformation = RoundedCornersTransformation(context, AdUtils.dp2px(context, cornerRadiusInDp.toFloat()), 0, cornerType)

        if (!imageUrl.isNullOrEmpty() && imageUrl!!.endsWith(".gif") && isSupportGif) {
            Glide.with(context)
                    .load(imageUrl)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imgView)
        } else {
            Glide.with(context)
                    .load(if (imageUrl.isNullOrEmpty()) imageUri else imageUrl)
                    .bitmapTransform(cornersTransformation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgView)
        }
    }

    @JvmStatic
    fun expandAdVertically(rlAd: ViewGroup?, maxHeightInDp: Int, animationDuration: Long) {
        rlAd?.apply {
            val valueAnimator = ValueAnimator.ofInt(0, maxHeightInDp)
            valueAnimator.addUpdateListener {
                layoutParams.height = it.animatedValue as Int
                requestLayout()
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    visibility = View.VISIBLE
                }
            })
            valueAnimator.duration = animationDuration
            valueAnimator.start()
        }
    }
}
