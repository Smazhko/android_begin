package ru.gb.m17_recyclerview.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.gb.m17_recyclerview.App
import ru.gb.m17_recyclerview.R
import ru.gb.m17_recyclerview.data.PhotoDTO
import ru.gb.m17_recyclerview.databinding.FragmentPhotoListBinding

@AndroidEntryPoint
class PhotoListFragment : Fragment() {
    private var _binding: FragmentPhotoListBinding? = null
    private val binding get () = _binding!!

    private val viewModel: PhotoListViewModel by viewModels()

    private val customAdapter = CustomAdapter{photo -> onItemClick(photo)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = customAdapter

        viewModel.photosStateFlow.onEach {
            customAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.buttonCrash.setOnClickListener {
            FirebaseCrashlytics.getInstance().log("This is log message with additional info")
            // try-catch превратили нашу попытку сломать приложение в некритичное уведомление в Crushlitics
            try {
                throw RuntimeException("CRASH button exception")
            }
            catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }

        binding.buttonNotify.setOnClickListener {
            createNotification()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            Log.d("FCM token", "TOKEN: " + it.result)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(item: PhotoDTO){
        val bundle = Bundle()
        bundle.putString("image_src", item.imgSrcHttps)

        val secondFragment = SinglePhotoFragment()
        secondFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .addToBackStack(null)
            .commit()
    }

    @SuppressLint("MissingPermission")
    fun createNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        else
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // NotificationCompat обеспечивает совместимость с предыдущими версиями Андроид
        val notification = NotificationCompat.Builder(requireContext(), App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("My first notification")
            .setContentText("Description of my 1st notification")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(requireContext()).notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1000
    }
}