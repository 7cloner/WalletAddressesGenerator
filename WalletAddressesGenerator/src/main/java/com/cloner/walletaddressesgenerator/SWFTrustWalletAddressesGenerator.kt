package com.cloner.walletaddressesgenerator

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import wallet.core.jni.CoinType
import wallet.core.jni.HDWallet
import wallet.core.jni.Mnemonic

object SWFTrustWalletAddressesGenerator {

    init {
        System.loadLibrary("TrustWalletCore")
    }

    private val resolvedCoins: Map<String, CoinType> = mapOf(
        "AETERNITY" to 457, "AION" to 425, "BINANCE" to 714, "BITCOIN" to 0,
        "BITCOINCASH" to 145, "BITCOINGOLD" to 156, "CALLISTO" to 820, "CARDANO" to 1815,
        "COSMOS" to 118, "PIVX" to 119, "DASH" to 5, "DECRED" to 42,
        "DIGIBYTE" to 20, "DOGE" to 3, "EOS" to 194, "ETHEREUM" to 60,
        "ETHEREUMCLASSIC" to 61, "FIO" to 235, "GOCHAIN" to 6060, "GROESTLCOIN" to 17,
        "ICON" to 74, "IOTEX" to 304, "KAVA" to 459, "KIN" to 2017,
        "LITECOIN" to 2, "NEBULAS" to 2718, "NULS" to 8964, "NANO" to 165,
        "NEAR" to 397, "NIMIQ" to 242, "ONTOLOGY" to 1024, "QTUM" to 2301,
        "XRP" to 144, "SOLANA" to 501, "STELLAR" to 148, "TEZOS" to 1729,
        "THETA" to 500, "THUNDERTOKEN" to 1001, "NEO" to 888, "TRON" to 195,
        "VECHAIN" to 818, "VIACOIN" to 14, "WANCHAIN" to 5718350, "ZCASH" to 133,
        "FIRO" to 136, "ZILLIQA" to 313, "ZELCASH" to 19167, "RAVENCOIN" to 175,
        "WAVES" to 5741564, "TERRA" to 330, "TERRAV2" to 10000330, "HARMONY" to 1023,
        "ALGORAND" to 283, "KUSAMA" to 434, "POLKADOT" to 354, "FILECOIN" to 461,
        "SMARTCHAIN" to 20000714, "OASIS" to 474, "POLYGON" to 966, "THORCHAIN" to 931,
        "BLUZELLE" to 483, "OPTIMISM" to 10000070, "ZKSYNC" to 10000324, "ARBITRUM" to 10042221,
        "XDAI" to 10000100, "FANTOM" to 10000250, "CRYPTOORG" to 394, "CELO" to 52752,
        "RONIN" to 10002020, "OSMOSIS" to 10000118, "ECASH" to 899, "IOST" to 291,
        "BOBA" to 10000288, "SYSCOIN" to 57, "VERGE" to 77, "ZEN" to 121,
        "METIS" to 10001088, "AURORA" to 1323161554, "EVMOS" to 10009001, "NATIVEEVMOS" to 20009001,
        "MOONRIVER" to 10001285, "MOONBEAM" to 10001284, "KAVAEVM" to 10002222, "METER" to 18000,
        "OKXCHAIN" to 996, "STRATIS" to 105105, "KOMODO" to 141, "NERVOS" to 309,
        "EVERSCALE" to 396, "APTOS" to 637, "HEDERA" to 3030, "SECRET" to 529,
        "NATIVEINJECTIVE" to 10000060, "AGORIC" to 564, "TON" to 607, "SUI" to 784,
        "STARGAZE" to 20000118, "POLYGONZKEVM" to 10001101, "JUNO" to 30000118, "STRIDE" to 40000118,
        "AXELAR" to 50000118, "CRESCENT" to 60000118, "KUJIRA" to 70000118, "IOTEXEVM" to 10004689,
        "NATIVECANTO" to 10007700, "COMDEX" to 80000118, "NEUTRON" to 90000118, "SOMMELIER" to 11000118,
        "MARS" to 13000118, "UMEE" to 14000118, "COREUM" to 10000990, "QUASAR" to 15000118,
        "PERSISTENCE" to 16000118, "AKASH" to 17000118, "NOBLE" to 18000118, "SCROLL" to 534352,
        "ROOTSTOCK" to 137, "ACALA" to 787, "ACALAEVM" to 10000787, "OPBNB" to 204,
        "NEON" to 245022934, "BASE" to 8453, "SEI" to 19000118, "LINEA" to 59144,
        "GREENFIELD" to 5600, "MANTLE" to 5000, "TIA" to 21000118, "ZETAEVM" to 20007000,
        "MERLIN" to 4200, "BLAST" to 81457, "BOUNCEBIT" to 6001, "PACTUS" to 21888,
        "SONIC" to 10000146
    ).mapValues { (_, value) -> CoinType.createFromValue(value) }

    fun generateAddress(
        coins: List<String>,
        seed: List<String>,
        passphrase: String = ""
    ): Flow<String> = flow {
        if (coins.isEmpty() || seed.isEmpty()) return@flow

        val mnemonic = seed.joinToString(separator = " ")
        if (!Mnemonic.isValid(mnemonic)) return@flow

        val wallet = try {
            HDWallet(mnemonic, passphrase)
        } catch (_: Exception) {
            return@flow
        }

        val size = coins.size
        for (i in 0 until size) {
            val coin = coins[i]
            val coinType = resolvedCoins[coin]
            if (coinType != null) {
                val address = wallet.getAddressForCoin(coinType)
                if (address.isNotEmpty()) {
                    emit(address)
                }
            }
        }
    }
}