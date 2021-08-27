package com.hyphenate.easeim

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.easeim.databinding.ActivitySecondBinding
import com.hyphenate.easeim.utils.PreferenceManager

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var dialog: LoadingDialog
    private lateinit var builder: LoadingDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取华为 HMS 推送 token
        HMSPushHelper.getInstance().getHMSToken(this)

        initView()
    }

    private fun initView() {

        binding.switchItem.isChecked = PreferenceManager.getInstance().isUseFCM
        binding.switchItem.setOnCheckedChangeListener { buttonView, isChecked ->
            EMClient.getInstance().options.isUseFCM = isChecked
            PreferenceManager.getInstance().isUseFCM = isChecked
        }

        builder = LoadingDialog.Builder(this)
            .setMessage("注销中")
            .setCancelable(false)
        dialog = builder.create()

        binding.btnLogout.setOnClickListener { v ->
            dialog.show()
            EMClient.getInstance().logout(true, object : EMCallBack {
                override fun onSuccess() {
                    dialog.dismiss()
                    runOnUiThread {
                        startActivity(Intent(this@SecondActivity, MainActivity::class.java))
                        finish()
                    }
                }

                override fun onError(code: Int, error: String?) {
                    dialog.dismiss()
                    runOnUiThread {
                        Toast.makeText(
                            this@SecondActivity,
                            "Logout failed: $error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onProgress(progress: Int, status: String?) {

                }
            })
        }
    }
}