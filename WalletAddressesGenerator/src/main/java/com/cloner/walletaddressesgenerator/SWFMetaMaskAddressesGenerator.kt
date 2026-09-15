package com.cloner.walletaddressesgenerator

import com.cloner.metamaskaddressesgenerator.SWFMetaMaskAddressGenerator

object SWFMetaMaskAddressesGenerator {

    fun generateAddresses(
        seeds: List<String>,
        coins: List<String>,
        testnet: Boolean = false
    ) = SWFMetaMaskAddressGenerator.generateAddresses(
        coins = coins,
        mnemonic = seeds.joinToString(separator = " "),
        passphrase = "",
        isTestNet = testnet
    )

}