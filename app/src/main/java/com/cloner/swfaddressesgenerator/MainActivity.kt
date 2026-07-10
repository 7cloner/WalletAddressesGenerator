package com.cloner.swfaddressesgenerator

import android.os.Bundle
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
            "knee", "utility", "still", "gorilla", "luxury", "milk",
            "response", "solve", "level", "enhance", "layer", "jeans",
            "cherry", "swift", "mother", "push", "situate", "menu",
            "negative", "dumb", "hundred", "outdoor", "foam", "evoke"
        )


        CoroutineScope(Dispatchers.IO).launch {
            //ton addresses
            val add = SWFTonAddressesGenerator.generateAddresses(
                seeds = words
            ).toMutableList()

            //TrustWallet
            val address = SWFTrustWalletAddressesGenerator.generateAddress(
                coin = "TON",
                seeds = words
            )
            if(address != null) {
                add.add(address)
            }

            //bitcoin
            add.addAll(
                SWFBitcoinAddressesGenerator.generateAddresses(
                    seeds = words,
                    bip = BitcoinImprovementProposals.P2PKH,
                    startAccountPosition = 0,
                    endAccountPosition = 2,
                    startChainPosition = 0,
                    endChainPosition = 15,
                    startAddressIndex = 0,
                    endAddressIndex = 20
                )
            )
        }
    }
}
