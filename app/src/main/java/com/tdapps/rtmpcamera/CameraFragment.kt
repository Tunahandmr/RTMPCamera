package com.tdapps.rtmpcamera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import cn.nodemedia.NodePublisher
import com.permissionx.guolindev.PermissionX
import com.tdapps.rtmpcamera.databinding.FragmentCameraBinding


class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val args: CameraFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PermissionX.init(requireActivity())
            .permissions(android.Manifest.permission.CAMERA)
            .request { allGranted, _, _ ->
                if (allGranted) {
                    //start the process
                    startPublishing()
                } else {
                    Toast.makeText(requireContext(), "permissions are required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun startPublishing() {
        val url = args.streamUrl ?: "rtmp://10.243.201.170:1935/live/streamkey"
        val nodePublisher = NodePublisher(requireContext(), "")
        nodePublisher.apply {
            attachView(binding.publishingLayout)
            setVideoOrientation(NodePublisher.VIDEO_ORIENTATION_PORTRAIT)
            setVideoCodecParam(
                NodePublisher.NMC_CODEC_ID_H264,
                NodePublisher.NMC_PROFILE_AUTO,
                480,   // Width (pixels)
                760,   // Height (pixels)
                30,     // Frame rate (fps)
                2_500_000 // Bit rate (bps)
            )
            openCamera(false)
            start(url)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}