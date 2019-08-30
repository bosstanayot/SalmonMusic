package tanayot.barjord.musicplayerapplication.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tanayot.barjord.musicplayerapplication.model.Song
import tanayot.barjord.musicplayerapplication.databinding.MusicItemBinding


class MusicListViewAdapter(var mItems: ArrayList<Song>?,
                           val listener: MusicListListener):RecyclerView.Adapter<MusicListViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItemBinding = MusicItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(listItemBinding, listener)
    }

    override fun getItemCount(): Int {
        return if(!mItems.isNullOrEmpty()){
            mItems!!.size
        }else{
            0
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mItems?.get(position))
    }

    fun setNewList(items: ArrayList<Song>){
        mItems = items
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: MusicItemBinding,
                     private val listener: MusicListListener):RecyclerView.ViewHolder(binding.root){
        fun bind(item: Song?){
            binding.song =  item
            binding.root.setOnClickListener {
                listener.onMusicClicked(item)
            }
        }
    }
}