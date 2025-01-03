package com.example.shakyafinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.shakyafinal.classroom.ClassroomDataHelper
import com.example.shakyafinal.databinding.FragmentMapBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // Fetch and display the classroom list
        lifecycleScope.launch {
            try {
                val classroomList = withContext(Dispatchers.IO) {
                    val helper = ClassroomDataHelper()
                    helper.getClassroomList()
                }

                // Display raw results in TextView
                binding.tv.text = classroomList.joinToString("\n") { it.toString() }
            } catch (e: Exception) {
                binding.tv.text = "Error: ${e.message}"
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
