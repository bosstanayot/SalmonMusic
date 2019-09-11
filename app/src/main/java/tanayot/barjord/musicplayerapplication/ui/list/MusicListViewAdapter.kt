package tanayot.barjord.musicplayerapplication.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import tanayot.barjord.musicplayerapplication.databinding.MusicItemBinding
import tanayot.barjord.musicplayerapplication.model.Music


class MusicListViewAdapter(private val listener: MusicListListener):PagedListAdapter<Music,  MusicListViewAdapter.ViewHolder>(Music.CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItemBinding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(listItemBinding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: MusicItemBinding,
                     private val listener: MusicListListener):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Music?){
            binding.music =  item
            binding.root.setOnClickListener {
                listener.onMusicClicked(item, adapterPosition)
            }
        }
    }
}