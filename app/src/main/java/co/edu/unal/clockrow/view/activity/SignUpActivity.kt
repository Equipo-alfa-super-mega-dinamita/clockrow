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
import co.edu.unal.clockrow.logic.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.editTextEmail
import kotlinx.android.synthetic.main.activity_sign_up.editTextPassword

class SignUpActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
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

            if (editTextEmail.text.isNotEmpty() && editTextPassword.text.isNotEmpty()  && editTextUsername.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(editTextEmail.text.toString(),
                    editTextPassword.text.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            storeNewUser(User(it.result?.user?.email ?: "", editTextUsername.text.toString(), ProviderType.EMAIL, false))
                            sendToHome(it.result?.user?.email ?: "", editTextUsername.text.toString(), ProviderType.EMAIL)
                        } else {
                            showSignUpAlert()
                        }
                }
            }
        }

        googleSignUpButton.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

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

    private fun sendToHome(email: String, username: String, provider: ProviderType){

       getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE).edit().putBoolean(getString(R.string.user_premium_shrpref), false).apply()
        verifyPremium(email)

        val homeIntent = Intent(this@SignUpActivity, MainActivity::class.java).apply {
            putExtra("EMAIL", email)
            putExtra("USERNAME", username)
            putExtra("PROVIDER", provider.name)
            putExtra("SOURCE", "SIGNUP")
        }
        startActivity(homeIntent)


    }

    private fun verifyPremium(email: String) {

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)
         db.collection("users").document(email).get().addOnCompleteListener {
             with (sharedPref.edit()) {
                 putBoolean(getString(R.string.user_premium_shrpref), it.result.getBoolean("premium")?: false)
                 commit()
             }
         }

    }

    companion object {
        private const val GOOGLE_SIGN_IN: Int = 100000
    }

    private fun showLoginAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de inicio de sesión")
        builder.setMessage("Las credenciales ingresadas no están registradas en la aplicación. Por favor intente nuevamente.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
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
                                    storeNewUser(User(account.email ?: "", account.displayName ?: "",  ProviderType.GOOGLE, false))
                                    sendToHome(account.email ?: "", account.displayName ?: "",  ProviderType.GOOGLE)
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

    private fun storeNewUser(user: User) {
        db.collection("users").document(user.email).set(user)
    }


}