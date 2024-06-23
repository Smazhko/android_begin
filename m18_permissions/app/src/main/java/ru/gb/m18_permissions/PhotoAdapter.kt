package ru.gb.m18_permissions

import android.view.LayoutInflater
import android.view.ViewGroup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gb.m18_permissions.databinding.ItemPhotoBinding
import ru.gb.m18_permissions.model.Photo

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var photos: List<Photo> = emptyList()

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            Glide.with(binding.imageView.context)
                .load(photo.filePath)
                .into(binding.imageView)
            binding.textViewDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).format(Date(photo.date))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setPhotos(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged()
    }
}