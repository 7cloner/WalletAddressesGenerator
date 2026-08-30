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

        val words =
            "chair cup solar fiscal apart whisper mouse fitness access obscure lounge apology sight lunch shine because senior cycle buddy card same tragic question eight".split(
                " "
            ).toList()


        CoroutineScope(Dispatchers.IO).launch {
            //ton addresses
            SWFTonAddressesGenerator.generateAddresses(
                seeds = words
            ).collect {
                Log.e("Address :", it)
            }

            //TrustWallet
            SWFTrustWalletAddressesGenerator.generateAddress(
                coins = listOf("BITCOIN"),
                seed = words
            ).collect {
                Log.e("Address :", it)
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
