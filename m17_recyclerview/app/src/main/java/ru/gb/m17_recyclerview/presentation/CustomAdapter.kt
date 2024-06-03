package ru.gb.m17_recyclerview.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gb.m17_recyclerview.R
import ru.gb.m17_recyclerview.data.PhotoDTO
import ru.gb.m17_recyclerview.databinding.PhotoListItemBinding

class CustomAdapter(
    private val onClick: (PhotoDTO) -> Unit
): RecyclerView.Adapter<PhotoViewHolder>() {
    private var data: List<PhotoDTO> = emptyList()

    fun setData(data: List<PhotoDTO>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            PhotoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
//        val item = data.getOrNull(position)
        val item = data.getOrNull(position)

        Log.d("ADAPTER", "onBindViewHolder: $data")

        with(holder.binding) {
            item?.let {
                Glide
                    .with(imageView.context)
                    .load(it.imgSrcHttps)
                    .into(imageView)
            }
            txtRover.text = holder.itemView.context.getString(R.string.txt_rover) + " " + (item?.rover?.name ?: "")
            txtCamera.text = holder.itemView.context.getString(R.string.txt_camera) + " " + (item?.camera?.name ?: "")
            txtSol.text = holder.itemView.context.getString(R.string.txt_id) + " " + (item?.id?.toString() ?: "")
            txtDate.text = holder.itemView.context.getString(R.string.txt_date) + " " + (item?.earthDate ?: "")
        }
        holder.binding.root.setOnClickListener {
            item?.let {
                onClick(item)
            }
        }
    }
}

class PhotoViewHolder (val binding: PhotoListItemBinding): RecyclerView.ViewHolder(binding.root)