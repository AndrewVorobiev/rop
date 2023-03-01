package com.test.rop

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.rop.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.util.Scanner


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            val ip = "193.124.118.129"
            val socket = Socket(ip, 80)

            val out = socket.getOutputStream()

            if (sharedPreferences.getBoolean("isNormal", false)) {
                out.write("normal".toByteArray(Charsets.UTF_8))
            }


            val br = BufferedReader(InputStreamReader(socket.getInputStream()))
            var cmd: String
            var C2S_Start = 0L
            while (br.readLine().also { cmd = it } != null) {
                Log.e("While", "Start")
                //Log.e("While",cmd)
                when (cmd) {
                    "ping" -> {
                        out.write("pong".toByteArray(Charsets.UTF_8))
                        out.flush()
                        Thread.sleep(50)
                        C2S_Start = System.currentTimeMillis()
                        out.write("ping".toByteArray(Charsets.UTF_8))
                    }
                    "pong" -> {
                        val ping = System.currentTimeMillis() - C2S_Start
                        out.write(("*$ping").toByteArray(Charsets.UTF_8))
                        Thread.sleep(40)
                    }

                    else -> {
//                    handleCommand(cmd, sharedPreferences)
                        Log.e("cmd", cmd)
                    }
                }
            }
    }
        catch (e: IOException) {
            e.message?.let { Log.e("While", it) };
        }
    }
}







