package com.cloner.swfaddressesgenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.cloner.walletaddressesgenerator.SWFBitcoinAddressesGenerator
import com.cloner.walletaddressesgenerator.SWFTonAddressesGenerator
import com.cloner.walletaddressesgenerator.SWFTrustWalletAddressesGenerator
import com.cloner.walletaddressesgenerator.enums.BitcoinImprovementProposals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val words = listOf(
            "abandon", "abandon", "abandon", "abandon", "abandon", "abandon",
            "abandon", "abandon", "abandon", "abandon", "abandon", "about"
        )


        CoroutineScope(Dispatchers.IO).launch {
            //ton addresses
            SWFTonAddressesGenerator.generateAddresses(
                seeds = words
            ).forEach {
                Log.e("Address :", it)
            }

            //TrustWallet
            val address = SWFTrustWalletAddressesGenerator.generateAddress(
                coin = "BITCOIN",
                seeds = words
            )
            if(address != null) {
                Log.e("Address :", address)
            }

            //bitcoin
            SWFBitcoinAddressesGenerator.generateAddresses(
                seeds = words,
                passphrase = "",
                bip = BitcoinImprovementProposals.SegWit,
                startAccountPosition = 0,
                endAccountPosition = 0,
                startChainPosition = 0,
                endChainPosition = 0,
                startAddressIndex = 0,
                endAddressIndex = 19,
                isTestNet = false
            ).collect {
                Log.e("Address :", it)
            }
        }
    }
}
