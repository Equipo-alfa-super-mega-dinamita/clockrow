package co.edu.unal.clockrow.view.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import co.edu.unal.clockrow.R
import kotlinx.android.synthetic.main.activity_premium.*
import kotlinx.android.synthetic.main.custom_dialog_fragment.*

class PremiumActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)

        val dialog = Dialog(this@PremiumActivity)
        dialog.setContentView(R.layout.custom_dialog_fragment)
        dialog.window?.setBackgroundDrawableResource(R.drawable.gradient)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)


        val button = dialog.findViewById<Button>(R.id.buttonAcceptPurchase)
        button.setOnClickListener {
            Toast.makeText(this, "VOLVER USUARIO PREMIUM", Toast.LENGTH_SHORT).show()

            dialog.dismiss()
        }

        upgradePremiumButton.setOnClickListener {
            showPurchaseAlert(dialog)
        }
    }

    private fun showPurchaseErrorAlert() {
        Toast.makeText(this, getString(R.string.purchase_error), Toast.LENGTH_SHORT).show()
    }

    private fun showPurchaseAlert(dialog: Dialog) {
        dialog.show()
    }

}