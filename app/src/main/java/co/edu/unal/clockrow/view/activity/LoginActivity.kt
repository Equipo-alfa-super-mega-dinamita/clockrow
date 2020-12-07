package co.edu.unal.clockrow.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import co.edu.unal.clockrow.ProviderType
import co.edu.unal.clockrow.R
import co.edu.unal.clockrow.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
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
}