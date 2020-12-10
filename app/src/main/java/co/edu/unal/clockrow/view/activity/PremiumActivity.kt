package co.edu.unal.clockrow.view.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import co.edu.unal.clockrow.ProviderType
import co.edu.unal.clockrow.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_premium.*
import kotlinx.android.synthetic.main.custom_dialog_fragment.*

class PremiumActivity : AppCompatActivity(){


    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        val dialog = Dialog(this@PremiumActivity)
        dialog.setContentView(R.layout.custom_dialog_fragment)
        dialog.window?.setBackgroundDrawableResource(R.drawable.gradient)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)

        dialog.setOnCancelListener {
            showPurchaseErrorAlert()
        }


        val button = dialog.findViewById<Button>(R.id.buttonAcceptPurchase)
        button.setOnClickListener {

            updateUserPremiumStatus()
            dialog.dismiss()
        }

        upgradePremiumButton.setOnClickListener {
            showPurchaseAlert(dialog)
        }
    }

    private fun updateUserPremiumStatus() {

        val sharedPref= getSharedPreferences( getString(R.string.shrpref_file), Context.MODE_PRIVATE)

        val email = sharedPref.getString(getString(R.string.user_email_shrpref), null)
        val username = sharedPref.getString(getString(R.string.user_username_shrpref), null)
        val provider = sharedPref.getString(getString(R.string.user_provider_shrpref), null)

        Log.i("VERIFICANDO SHR PREF", "$email, $username, $provider")

        if (email != null && provider != null && username != null) {
            db.collection("users").document(email).update("premium", true)
            sharedPref.edit().putBoolean(getString(R.string.user_premium_shrpref), true)
            Toast.makeText(this, "Se ha actualizado a la versión premium", Toast.LENGTH_LONG).show()
            val homeIntent = Intent(this@PremiumActivity, MainActivity::class.java)
            startActivity(homeIntent)
        }
        else {
            Toast.makeText(this, "¡Debes estar registrado para actualizar a la versión premium!", Toast.LENGTH_LONG).show()
            val homeIntent = Intent(this@PremiumActivity, SignUpActivity::class.java)
            startActivity(homeIntent)
        }
    }

    private fun showPurchaseErrorAlert() {
        Toast.makeText(this, getString(R.string.purchase_error), Toast.LENGTH_SHORT).show()
    }

    private fun showPurchaseAlert(dialog: Dialog) {
        dialog.show()
    }

}