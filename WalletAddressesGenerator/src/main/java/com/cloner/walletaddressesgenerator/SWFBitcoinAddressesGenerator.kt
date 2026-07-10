package com.cloner.walletaddressesgenerator

import com.cloner.walletaddressesgenerator.enums.BitcoinImprovementProposals
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.crypto.ChildNumber
import com.google.common.collect.ImmutableList
import org.bitcoinj.base.ScriptType
import org.bitcoinj.base.SegwitAddress
import org.bitcoinj.script.ScriptBuilder
import org.bitcoinj.wallet.DeterministicKeyChain

@Suppress("DEPRECATION")
object SWFBitcoinAddressesGenerator {

    fun generateAddresses(
        seeds: List<String>,
        bip: BitcoinImprovementProposals,
        startAccountPosition: Int,
        endAccountPosition: Int,
        startChainPosition: Int,
        endChainPosition: Int,
        startAddressIndex: Int,
        endAddressIndex: Int
    ): List<String> {
        val params: NetworkParameters = MainNetParams.get()!!
        val allAddresses = mutableListOf<String>()
        val seed = DeterministicSeed(seeds.joinToString(separator = " "), null, "", 0L)
        val rootChain = DeterministicKeyChain.builder().seed(seed).build()

        val purposes = bip.purposes
        for (purpose in purposes) {
            for (account in startAccountPosition..endAccountPosition) {
                for (chain in startChainPosition..endChainPosition) {
                    for (index in startAddressIndex..endAddressIndex) {

                        val path = createPath(purpose, account, chain, index)
                        val key = rootChain.getKeyByPath(path, true)

                        val address = when (purpose) {
                            44 -> key.toAddress(ScriptType.P2PKH, params.network()).toString()
                            49 -> {
                                val script = ScriptBuilder.createP2SHOutputScript(
                                    ScriptBuilder.createP2WPKHOutputScript(key)
                                )
                                script.getToAddress(params).toString()
                            }

                            84 -> key.toAddress(ScriptType.P2WPKH, params.network()).toString()
                            86 -> {
                                val xOnly = key.pubKey.sliceArray(1 until key.pubKey.size)
                                SegwitAddress.fromProgram(params, 1, xOnly).toString()
                            }

                            else -> ""
                        }

                        if (address.isNotEmpty()) {
                            allAddresses.add(address)
                        }
                    }
                }
            }
        }
        return allAddresses
    }

    private fun createPath(
        purpose: Int,
        account: Int,
        chain: Int,
        index: Int
    ): ImmutableList<ChildNumber> {
        return ImmutableList.of(
            ChildNumber(purpose, true),
            ChildNumber(0, true),
            ChildNumber(account, true),
            ChildNumber(chain),
            ChildNumber(index, false)
        )
    }

}