package com.cloner.walletaddressesgenerator.enums

enum class BitcoinImprovementProposals(val purposes: List<Int>) {


    ALL(listOf(44, 49, 84, 86)),//tested
    P2PKH(listOf(44)),//tested
    P2SH(listOf(49)),//tested
    SegWit(listOf(84)),//tested
    Taproot(listOf(86)) // tested

}