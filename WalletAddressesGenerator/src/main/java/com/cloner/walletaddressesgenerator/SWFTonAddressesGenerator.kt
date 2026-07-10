package com.cloner.walletaddressesgenerator

import com.cloner.tonaddressgenerator.TonKeeperV1AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV2AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV3AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV4AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV5AddressGenerator

object SWFTonAddressesGenerator {

    private val allAlgorithms = listOf(
        "V1R1", "V1R2", "V1R3", "V2R1", "V2R2", "V3R1", "V3R2",
        "V4R1", "V4R2", "V5R1", "V5BETA"
    )
    private val v1Generator = TonKeeperV1AddressGenerator()
    private val v2Generator = TonKeeperV2AddressGenerator()
    private val v3Generator = TonKeeperV3AddressGenerator()
    private val v4Generator = TonKeeperV4AddressGenerator()
    private val v5Generator = TonKeeperV5AddressGenerator()

    fun generateAddresses(
        seeds: List<String>,
        algorithms: List<String> = emptyList(),
        testnet: Boolean = false
    ): List<String> {
        val addresses: MutableList<String> = ArrayList()

        algorithms.ifEmpty { allAlgorithms }.forEach { algorithm ->
            val address = generateAddress(
                seeds = seeds,
                algorithm = algorithm.uppercase(),
                testnet = testnet
            )
            if(address != null){
                addresses.add(address)
            }
        }

        return addresses
    }

    private fun generateAddress(
        seeds: List<String>,
        algorithm: String,
        testnet: Boolean
    ): String? {
        return when (algorithm) {
            "V1R1" -> v1Generator.getR1Address(seeds = seeds, testnet = testnet)
            "V1R2" -> v1Generator.getR2Address(seeds = seeds, testnet = testnet)
            "V1R3" -> v1Generator.getR3Address(seeds = seeds, testnet = testnet)
            "V2R1" -> v2Generator.getR1Address(seeds = seeds, testnet = testnet)
            "V2R2" -> v2Generator.getR2Address(seeds = seeds, testnet = testnet)
            "V3R1" -> v3Generator.getR1Address(seeds = seeds, testnet = testnet)
            "V3R2" -> v3Generator.getR2Address(seeds = seeds, testnet = testnet)
            "V4R1" -> v4Generator.getR1Address(seeds = seeds, testnet = testnet)
            "V4R2" -> v4Generator.getR2Address(seeds = seeds, testnet = testnet)
            "V5R1" -> v5Generator.getR1Address(seeds = seeds, testnet = testnet)
            "V5BETA" -> v5Generator.getBetaAddress(seeds = seeds, testnet = testnet)
            else -> null
        }
    }
}