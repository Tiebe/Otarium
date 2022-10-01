package nl.tiebe.openbaarlyceumzeist.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import nl.tiebe.openbaarlyceumzeist.android.R
import nl.tiebe.openbaarlyceumzeist.android.databinding.FragmentDebugBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DebugFragment : Fragment() {

    private var _binding: FragmentDebugBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDebugBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
/*
        binding.buttonClear.setOnClickListener {
            Tokens.clearTokens()
            database.clearDatabase()
        }

        binding.buttonClearDB.setOnClickListener {
            database.clearDatabase()
        }

        binding.buttonTestNotify.setOnClickListener {
            Thread {
                database.removeRandomGrade()
                Background().updatePeriodically(binding.root.context)
            }.start()

        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}