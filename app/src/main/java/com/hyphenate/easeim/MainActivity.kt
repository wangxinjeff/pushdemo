package com.hyphenate.easeim

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.easeim.databinding.ActivityMainBinding
import com.hyphenate.exceptions.HyphenateException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var etAccount: AppCompatEditText
    private lateinit var etPassword: AppCompatEditText
    private lateinit var dialog: LoadingDialog
    private lateinit var builder: LoadingDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(EMClient.getInstance().isLoggedInBefore){
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        builder = LoadingDialog.Builder(this)
            .setMessage("注册中")
            .setCancelable(false)
        dialog = builder.create()

        etAccount = binding.etAccount
        etPassword = binding.etPassword
        binding.btnRegist.setOnClickListener { v ->
            builder.updateMessage("注册中")
            dialog.show()
            Thread(Runnable {
                try {
                    EMClient.getInstance().createAccount(
                        etAccount.text.toString(),
                        etPassword.text.toString()
                    )
                    dialog.dismiss()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Regist success", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: HyphenateException) {
                    dialog.dismiss()
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Regist failed: "+ e.errorCode + "/" +e.description, Toast.LENGTH_SHORT).show()
                    }
                }

            }).start()
        }

        binding.btnLogin.setOnClickListener { v ->
            builder.updateMessage("登录中")
            dialog.show()
            EMClient.getInstance().login(
                etAccount.text.toString(),
                etPassword.text.toString(),
                object: EMCallBack{
                    override fun onSuccess() {
                        dialog.dismiss()
                        runOnUiThread {
                            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
                            finish()
                        }
                    }

                    override fun onError(code: Int, error: String?) {
                        dialog.dismiss()
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Login failed: $error", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onProgress(progress: Int, status: String?) {

                    }
                }
            )
        }
    }
}