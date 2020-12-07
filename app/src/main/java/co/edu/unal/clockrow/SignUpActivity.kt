package co.edu.unal.clockrow

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
import co.edu.unal.clockrow.view.activity.LoginActivity
import co.edu.unal.clockrow.view.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        val textViewLogin = findViewById<TextView>(R.id.textViewLogin)
        val registerText = "¿Ya tienes una cuenta? Inicia sesión"
        val ss = SpannableString(registerText)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(this@SignUpActivity, "Registrar", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        ss.setSpan(clickableSpan, 23, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textViewLogin.text = ss
        textViewLogin.movementMethod = LinkMovementMethod.getInstance()

        setup()

    }

    private fun setup() {

        signUpButton.setOnClickListener {

            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextEmail.text.toString(),
                    editTextPassword.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            sendToHome(it.result?.user?.email ?: "", ProviderType.EMAIL)
                        } else {
                            showSignUpAlert()
                        }
                }
            }
        }
    }

    private fun showSignUpAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de registro")
        builder.setMessage("No ha sido posible autenticar el usuario ingresado. Por favor intente nuevamente.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun sendToHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this@SignUpActivity, MainActivity::class.java).apply {

            putExtra("EMAIL", email)
            putExtra("PROVIDER", provider.name)
            putExtra("SOURCE", "SIGNUP")
        }
        startActivity(homeIntent)


    }



}