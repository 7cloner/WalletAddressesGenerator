package com.cloner.walletaddressesgenerator

import com.cloner.tonaddressgenerator.TonKeeperV1AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV2AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV3AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV4AddressGenerator
import com.cloner.tonaddressgenerator.TonKeeperV5AddressGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SWFTonAddressesGenerator {

    private val allAlgorithms = listOf(
        "V1R1", "V1R2", "V1R3", "V2R1", "V2R2", "V3R1", "V3R2",
        "V4R1", "V4R2", "V5R1", "V5BETA"
    )

    private val v1Generator by lazy { TonKeeperV1AddressGenerator() }
    private val v2Generator by lazy { TonKeeperV2AddressGenerator() }
    private val v3Generator by lazy { TonKeeperV3AddressGenerator() }
    private val v4Generator by lazy { TonKeeperV4AddressGenerator() }
    private val v5Generator by lazy { TonKeeperV5AddressGenerator() }

    fun generateAddresses(
        seeds: List<String>,
        algorithms: List<String> = emptyList(),
        testnet: Boolean = false
    ): Flow<String> = flow {
        if (seeds.isEmpty()) return@flow

        val targetAlgorithms = algorithms.ifEmpty { allAlgorithms }
        val size = targetAlgorithms.size

        for (i in 0 until size) {
            val algorithm = targetAlgorithms[i]
            val address = generateAddress(
                seeds = seeds,
                algorithm = algorithm,
                testnet = testnet
            )
            if (!address.isNullOrEmpty()) {
                emit(address)
            }else {
                break
            }
        }
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