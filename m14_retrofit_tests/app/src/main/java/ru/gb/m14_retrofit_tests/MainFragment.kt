package ru.gb.m14_retrofit_tests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.m14_retrofit_tests.databinding.FragmentMainBinding
import kotlin.concurrent.thread

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val jsonUser = """{
//            "results": [
//            {
//                "gender": "female",
//                "cell": "0920-660-6643",
//                "nat": "IR"
//            }
//            ],
//            "info": {
//            "seed": "e4e4990378be59e0",
//            "results": 1,
//            "page": 1,
//            "version": "1.4"
//        }
//        }
//        """
//
//        val userAdapter = Gson().getAdapter(UserResponse::class.java)
//        val newUser: User = userAdapter.fromJson(jsonUser).results.first()
//
//        thread {
//            val response = RetrofitInstance.searchApi.getUser().execute()
//            binding.txt.text = response.body()?.toString()
//        }




        RetrofitInstance.searchApi.getUser().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val newUser = response.body()?.results?.first() ?: return
                    //binding.txt.text = newUser.nameComposite
                    binding.txt.text = "${newUser.name.title} ${newUser.name.first} ${newUser.name.last}"
                    binding.img.load(newUser.picture.large)

                    Log.d("TAG >>>>> ", "onResponse: ${newUser.nameComposite}")
                    Log.d("TAG >>>>> ", "onResponse: ${newUser.locationComposite}")
                    Log.d("TAG >>>>> ", "onResponse: ${newUser.dobComposite}")

                } else {
                    Log.e("ERROR", "onResponse: ${response.code()}")
                }
            }

            //  лог ошибки
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("NETWORK >>>", "Что-то пошло не так", t)
            }
        })
    }

}