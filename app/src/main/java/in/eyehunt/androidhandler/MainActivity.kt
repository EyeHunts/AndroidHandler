package `in`.eyehunt.androidhandler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var mHandlerThread: Handler
    lateinit var mThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar.max = 100

        mThread = Thread(Runnable {
            for (i in 0..100) {
                Log.d("I", ":$i")
                progressBar.progress = i
                try {
                    Thread.sleep(100)
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }

                val message = Message()
                message.what = COUNT
                message.arg1 = i
                mHandlerThread.sendMessage(message)
            }
        })


        // start counting
        start_progress.setOnClickListener {
            if (!mThread.isAlive) {
                val currentProgess = progressBar.progress
                mHandlerThread.sendEmptyMessage(START)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mHandlerThread = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what === START) {
                    mThread.start()
                } else if (msg.what === COUNT) {
                    textView.text = "Count " + msg.arg1
                }
            }
        }
    }

    companion object {
        private const val START = 100
        private const val COUNT = 101
    }
}
