package mx.com.sfinx.app.myapplication

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG: String = MainActivity::class.java.simpleName
        val callbackManager: CallbackManager = CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val packageName: String = applicationContext.packageName
        val packageInfo: PackageInfo = applicationContext.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        val signature = packageInfo.signatures[0].toByteArray()
        val messageDigest: MessageDigest = MessageDigest.getInstance("SHA")
        val keyHash: String = Base64.encodeToString(messageDigest.digest(signature), Base64.NO_WRAP)
        println("Key Hash is: $keyHash")

        val loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions("email")

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d(TAG, "onSuccess: ${result?.accessToken?.token}")
            }

            override fun onCancel() {
                Log.w(TAG, "onCancel: auth is canceled")
            }

            override fun onError(error: FacebookException?) {
                Log.e(TAG, "onError: ${error?.message}")
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}