package com.example.applemarket

import ProductViewModel
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.R
import com.example.applemarket.product.ProductListAdapter
import com.example.applemarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

        binding.imgbtnAlarm.setOnClickListener {
            notification()
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0 && binding.fab.isShown) {
                    binding.fab.hide()
                } else if (dy > 0 && !binding.fab.isShown) {
                    binding.fab.show()
                }
            }
        })

        binding.fab.setOnClickListener {
            // 최상단으로 스크롤
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showExitDialog()
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productAdapter = ProductListAdapter(productViewModel) // ProductViewModel 전달

        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun showExitDialog() {
        val dialogView = layoutInflater.inflate(R.layout.exit_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()

        val btnOk = dialogView.findViewById<Button>(R.id.btn_ok)
        val btnCancle = dialogView.findViewById<Button>(R.id.btn_cancle)

        btnOk.setOnClickListener {
            finish()
        }

        btnCancle.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun notification() { //Notification

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel_id"
        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0 이상
            val channel = NotificationChannel(
                channelId, "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                // 채널에 다양한 정보 설정
                description = "channel_description"
                setShowBadge(true)
            }

            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(this, channelId)
        } else { // Android 8.0 이하
            builder = NotificationCompat.Builder(this)
        }

        builder.run {
            setSmallIcon(R.drawable.hun1)
            setContentTitle("키워드 알림")
            setContentText("설정한 키워드에 대한 알림이 도착했습니다.")
            priority = NotificationCompat.PRIORITY_DEFAULT
            setWhen(System.currentTimeMillis())
        }

        notificationManager.notify(1000, builder.build())
    }
}
