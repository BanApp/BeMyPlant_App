package com.example.bemyplant.fragment

import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.bemyplant.R
import com.example.bemyplant.databinding.FragmentS2Binding
import com.example.bemyplant.data.SignUpData
import com.example.bemyplant.network.RetrofitService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [s2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class s2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val binding by lazy{FragmentS2Binding.inflate((layoutInflater))}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.button3.setOnClickListener {
            val signUpData = getSignUpData()
            val pw2 = binding.editText3.text.toString()
            if (signUpData.username.isEmpty()){
                showToast(requireContext(),"아이디를 입력해주세요.")
            } else if (signUpData.password.isEmpty()){
                showToast(requireContext(),"비밀번호를 입력해주세요.")
            } else if (signUpData.password != pw2) {
                showToast(requireContext(),"비밀번호가 일치하지 않습니다.")
            } else{
                signUp(signUpData)
            }
        }
        return binding.root
    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    private val retrofitService = RetrofitService().apiService

    private fun getSignUpData(): SignUpData {
        val username = binding.editText1.text.toString()
        val pw = binding.editText2.text.toString()
        val r_name = arguments?.getString("r_name").toString()
        val phones = arguments?.getString("phones").toString()
        val date = Date()

        return SignUpData(username, pw, phones, r_name, date, 1)
    }
    private fun signUp(signUpData: SignUpData){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 요청 보내기
                val response = retrofitService.signUp(signUpData)

                if (response.isSuccessful) {
                    // 회원 가입 성공
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "회원가입 완료")
                        findNavController().navigate(R.id.action_s2Fragment_to_pRFragment)
                    }
                } else {
                    // 회원 가입 실패
                    withContext(Dispatchers.Main) {
                        showToast(requireContext(), "회원가입 실패")
                    }
                }
            } catch (e: Exception) {
                // API 요청 실패
                withContext(Dispatchers.Main) {
                    showToast(requireContext(), "API 요청 실패: ${e.message}")
                }
            }

        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            s2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}