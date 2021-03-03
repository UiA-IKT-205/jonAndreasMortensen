package no.uia.ikt205.superpiano

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import no.uia.ikt205.superpiano.databinding.FragmentHalfTonePianoKeyBinding
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.view.*

class HalfTonePianoKeyFragment : Fragment() {

    private var _binding:FragmentHalfTonePianoKeyBinding? = null
    private val binding get() = _binding!!

    private lateinit var note:String

    var onKeyPressed:((note:String) -> Unit)? = null
    var onKeyReleased:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHalfTonePianoKeyBinding.inflate(inflater)
        val view = binding.root

        view.halfToneKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@HalfTonePianoKeyFragment.onKeyPressed?.invoke(note)
                    MotionEvent.ACTION_UP -> this@HalfTonePianoKeyFragment.onKeyReleased?.invoke(note)
                }
                return true
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
                HalfTonePianoKeyFragment().apply {
                    arguments = Bundle().apply {
                        putString("NOTE", note)
                    }
                }
    }
}