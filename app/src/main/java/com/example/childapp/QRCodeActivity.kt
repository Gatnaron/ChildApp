package com.example.childapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.childapp.databinding.ActivityQrcodeBinding
import java.util.UUID

class QRCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrcodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uniqueId = UUID.randomUUID().toString()
        val firstPart = uniqueId.substring(0, 8)
        val lastPart = uniqueId.substring(uniqueId.length - 8)

        binding.textViewFirstPart.text = firstPart
        binding.textViewLastPart.text = lastPart
    }
}
