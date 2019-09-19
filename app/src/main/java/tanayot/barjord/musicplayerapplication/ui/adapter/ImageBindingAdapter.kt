package tanayot.barjord.musicplayerapplication.ui.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object ImageBindingAdapter{
    @JvmStatic
    @BindingAdapter("musicImage", "progressBar", requireAll = false)
    fun loadImage(imageView: ImageView, imageURL: String?, progressBar: ProgressBar?) {
        progressBar?.visibility = View.VISIBLE
        if(!imageURL.isNullOrEmpty()){
            Glide.with(imageView.context)
                .load(imageURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar?.visibility = View.GONE
                        return false
                    }

                })
                .into(imageView)
        }
    }
}