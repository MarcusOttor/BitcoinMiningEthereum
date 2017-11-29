package com.freebitcoins.btcmining.satoshi.screens.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import butterknife.ButterKnife
import butterknife.OnClick
import com.freebitcoins.btcmining.satoshi.R

class CurrenciesDialog(private var pick: (String) -> Unit) : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)

        var view = inflater?.inflate(R.layout.currencies_dialog, container, false)

        ButterKnife.bind(this, view!!)

        return view
    }

    @OnClick(R.id.bitcoin, R.id.ethereum, R.id.litecoin, R.id.zcash, R.id.dash, R.id.ripple, R.id.monero)
    fun ok(view: View) {
        when (view.id) {
            R.id.bitcoin -> pick("Bitcoin")
            R.id.litecoin -> pick("Litecoin")
            R.id.ethereum -> pick("Ethereum")
            R.id.zcash -> pick("Zcash")
            R.id.dash -> pick("Dash")
            R.id.ripple -> pick("Ripple")
            R.id.monero -> pick("Monero")
        }
        dismiss()
    }
}