package co.edu.unal.clockrow.view.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import co.edu.unal.clockrow.ProviderType
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.SignUpActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.editTextEmail
import kotlinx.android.synthetic.main.activity_login.editTextPassword
import kotlinx.android.synthetic.main.activity_sign_up.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val textViewSignUp = findViewById<TextView>(R.id.textViewLogin)
        val registerText = "¿No estás registrado aún? Registrate"
        val ss = SpannableString(registerText)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(this@LoginActivity, "Registrar", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
        ss.setSpan(clickableSpan, 26, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textViewSignUp.text = ss
        textViewSignUp.movementMethod = LinkMovementMethod.getInstance()

        setup()

        session()




    }

    private fun session() {

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)
        val email = sharedPref.getString(getString(R.string.user_email_shrpref), null)
        val provider = sharedPref.getString(getString(R.string.user_provider_shrpref), null)

        if (email != null && provider != null) {
            sendToHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        loginButton.setOnClickListener {
            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(editTextEmail.text.toString(),
                        editTextPassword.text.toString()).addOnCompleteListener {

                    if (it.isSuccessful) {
                        sendToHome(it.result?.user?.email ?: "", ProviderType.EMAIL)
                    } else {
                        showLoginAlert()
                    }
                }
            }
        }

        googleLoginButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }

    }

    private fun sendToHome(email: String, provider: ProviderType) {

        val homeIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
            putExtra("EMAIL", email)
            putExtra("PROVIDER", provider.name)
            putExtra("SOURCE", "LOGIN")
        }
        startActivity(homeIntent)
    }

    private fun showLoginAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de inicio de sesión")
        builder.setMessage("Las credenciales ingresadas no están registradas en la aplicación. Por favor intente nuevamente.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    companion object {
        private const val GOOGLE_SIGN_IN: Int = 100
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    sendToHome(account.email ?: "", ProviderType.GOOGLE)
                                } else {
                                    showLoginAlert()
                                }
                            }
                }
            } catch (e : ApiException){
                Log.e("GOOGLE_LOGIN", e.stackTrace.toString())

            }
        }
    }
}