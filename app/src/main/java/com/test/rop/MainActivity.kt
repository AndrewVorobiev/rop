package com.test.rop

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.rop.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var socket: Socket
    private lateinit var out: OutputStream
    private lateinit var input: InputStream


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Thread {
            val sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)


            try {
                val ip = "193.124.118.129"
                val socket = Socket(ip, 80)
                if (socket.isConnected)
                    out = socket.getOutputStream()
                input = socket.getInputStream()
                if (sharedPreferences.getBoolean("isNormal", false)) {

                    out.write("normal".toByteArray(Charsets.UTF_8))
                }

            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            try {
                val br = BufferedReader(InputStreamReader(socket.getInputStream()))
                var cmd: String
                var timerStart = 0L

                while (br.readLine().also { cmd = it } != null) {
                    Log.e("While", "Start")
                    //Log.e("While",cmd)
                    when (cmd) {
                        "ping" -> {
                            out.write("pong".toByteArray(Charsets.UTF_8))
                            out.flush()
                            println(out.write("pong".toByteArray(Charsets.UTF_8)))
                            Thread.sleep(50)
                            timerStart = System.currentTimeMillis()
                            out.write("ping".toByteArray(Charsets.UTF_8))
                        }
                        "pong" -> {
                            val ping = System.currentTimeMillis() - timerStart
                            out.write(
                                ("*$ping}").toByteArray(
                                    Charsets.UTF_8
                                )
                            )
                            Thread.sleep(40)
                        }

                        else -> {
                            Log.e("cmd", cmd)
                        }
                    }
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

    }
}







