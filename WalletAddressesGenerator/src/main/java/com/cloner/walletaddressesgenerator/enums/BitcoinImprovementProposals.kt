package com.cloner.walletaddressesgenerator.enums

enum class BitcoinImprovementProposals(val purposes: List<Int>) {


    ALL(listOf(44, 49, 84, 86)),
    P2PKH(listOf(44)),
    P2SH(listOf(49)),
    SegWit(listOf(84)),
    Taproot(listOf(86))

}