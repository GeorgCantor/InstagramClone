package com.georgcantor.instagramclone.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.georgcantor.instagramclone.R
import com.georgcantor.instagramclone.model.response.Hit
import com.georgcantor.instagramclone.util.loadCircleImage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FeedAdapter(
    private val pictures: List<Hit>,
) : ListAdapter<Hit, FeedAdapter.FeedViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FeedViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
    )

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val pic = pictures[position]
        val images = listOf(pic.webformatURL, pic.webformatURL, pic.webformatURL)
        with(holder) {
            itemView.context.loadCircleImage(pic.userImageURL, icon)
            name.text = pic.user
            indices.text = "${viewPager.currentItem}/${images.size}"
            viewPager.adapter = PagerAdapter(images)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setPageIndex(position + 1, images.size)
                    super.onPageSelected(position)
                }
            })
            TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()

            fav.setOnClickListener {
                fav.setImageResource(
                    when (fav.drawable.constantState) {
                        getDrawable(itemView.context, R.drawable.ic_favorite)?.constantState -> R.drawable.ic_favorite_red
                        getDrawable(itemView.context, R.drawable.ic_favorite_red)?.constantState -> R.drawable.ic_favorite
                        else -> R.drawable.ic_favorite
                    }
                )
            }
        }
    }

    override fun getItemCount() = pictures.size

    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.icon)
        val name: TextView = view.findViewById(R.id.name)
        val indices: TextView = view.findViewById(R.id.pager_indices)
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)
        val fav: ImageView = view.findViewById(R.id.fav)

        fun setPageIndex(index: Int, allIndex: Int) {
            indices.text = "$index/$allIndex"
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(old: Hit, new: Hit) = old == new
        override fun areContentsTheSame(old: Hit, new: Hit) = old.id == new.id
    }
}