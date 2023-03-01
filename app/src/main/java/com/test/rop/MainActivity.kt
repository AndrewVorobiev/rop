package com.test.rop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.rop.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket
import java.util.Scanner


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var active: Boolean = false
    private var data: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val address = "194.87.99.239"
        val port = 80
        binding.buttonConnect.setOnClickListener{
            if(binding.buttonConnect.text == "connect"){
                binding.buttonConnect.text = "disconnect"
                active = true
                CoroutineScope(Dispatchers.IO).launch {
                    client(address,port)
                }
            } else {
                active = false
                binding.buttonConnect.text = "connect"
            }

        }

    }
    private suspend fun client(address: String, port: Int){
        val connection = Socket(address, port)
        val writer = connection.getOutputStream()
        writer.write(1)
        val reader = Scanner(connection.getInputStream())
        while(active){
            var input = ""
            input = reader.nextLine()
            if(data.length < 300) {
                data += "\n$input"
            }
            else{
                data = input
                binding.tvServerResponse.text = data
            }
            reader.close()
            writer.close()
            connection.close()
        }
    }
}






