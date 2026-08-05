package com.cloner.walletaddressesgenerator

import com.cloner.walletaddressesgenerator.enums.BitcoinImprovementProposals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.params.TestNet3Params
import org.bitcoinj.wallet.DeterministicSeed
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.base.ScriptType
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException
import org.bitcoinj.script.ScriptBuilder
import wallet.core.jni.AnyAddress
import wallet.core.jni.CoinType
import wallet.core.jni.Derivation
import wallet.core.jni.HDWallet
import java.time.Instant

object SWFBitcoinAddressesGenerator {

    fun generateAddresses(
        seeds: List<String>,
        isTestNet: Boolean,
        bip: BitcoinImprovementProposals,
        startAccountPosition: Int,
        endAccountPosition: Int,
        startChainPosition: Int,
        endChainPosition: Int,
        startAddressIndex: Int,
        endAddressIndex: Int,
        passphrase: String = ""
    ): Flow<String> = flow {
        if (seeds.isEmpty()) return@flow

        try {
            MnemonicCode.INSTANCE.check(seeds)
        } catch (_: MnemonicException) {
            return@flow
        }


        if (startAccountPosition < 0 || endAccountPosition < 0 ||
            startAccountPosition > endAccountPosition
        ) return@flow

        if (startChainPosition < 0 || endChainPosition < 0 ||
            startChainPosition > endChainPosition
        ) return@flow

        if (startAddressIndex < 0 || endAddressIndex < 0 ||
            startAddressIndex > endAddressIndex
        ) return@flow


        val params: NetworkParameters = if (isTestNet) TestNet3Params.get()!! else MainNetParams.get()!!
        val network = params.network()
        val coinType = if (isTestNet) 1 else 0


        val mnemonic = seeds.joinToString(separator = " ")
        val wallet = HDWallet(mnemonic, passphrase)
        val seed = DeterministicSeed.ofMnemonic(mnemonic, passphrase, Instant.EPOCH)
        val rootKey = HDKeyDerivation.createMasterPrivateKey(seed.seedBytes)

        val purposes = bip.purposes
        for (purpose in purposes) {
            val purposeKey = HDKeyDerivation.deriveChildKey(rootKey, ChildNumber(purpose, true))
            val coinTypeKey = HDKeyDerivation.deriveChildKey(purposeKey, ChildNumber(coinType, true))

            for (account in startAccountPosition..endAccountPosition) {
                val accountKey = HDKeyDerivation.deriveChildKey(coinTypeKey, ChildNumber(account, true))

                for (chain in startChainPosition..endChainPosition) {
                    val chainKey = HDKeyDerivation.deriveChildKey(accountKey, ChildNumber(chain, false))

                    for (index in startAddressIndex..endAddressIndex) {
                        val key = HDKeyDerivation.deriveChildKey(chainKey, ChildNumber(index, false))

                        val address = when (purpose) {
                            44 -> key.toAddress(ScriptType.P2PKH, network).toString()
                            49 -> {
                                val script = ScriptBuilder.createP2SHOutputScript(
                                    ScriptBuilder.createP2WPKHOutputScript(key)
                                )
                                script.getToAddress(network, true).toString()
                            }
                            84 -> key.toAddress(ScriptType.P2WPKH, network).toString()
                            86 -> {
                                deriveTaprootAddress(
                                    wallet = wallet,
                                    coinType = if(isTestNet) 1 else 0,
                                    account = account,
                                    chain = chain,
                                    index = index
                                )
                            }
                            else -> ""
                        }

                        if (address.isNotEmpty()) {
                            emit(address)
                        }
                    }
                }
            }
        }
    }

    private fun deriveTaprootAddress(
        wallet: HDWallet,
        coinType: Int,
        account: Int,
        chain: Int,
        index: Int
    ): String {
        val path = "m/86'/$coinType'/$account'/$chain/$index"
        val privateKey = wallet.getKey(CoinType.BITCOIN, path)
        val publicKey = privateKey.getPublicKeySecp256k1(true)
        val taprootAddress = AnyAddress(publicKey, CoinType.BITCOIN, Derivation.BITCOINTAPROOT)
        return taprootAddress.description()
    }

}