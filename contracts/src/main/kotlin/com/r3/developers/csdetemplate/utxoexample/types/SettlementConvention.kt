package com.r3.developers.csdetemplate.utxoexample.types
/*
import net.corda.v5.base.annotations.CordaSerializable

/**
 * Defines the settlement day conventions
 */
@CordaSerializable
enum class SettlementConvention(val display: String) {
    T_0("T+0"),
    T_1("T+1"),
    T_2("T+2"),
    T_3("T+3");

    companion object {
        private val regexT0 = Regex("\\w[-+_=/]0")
        private val regexT1 = Regex("\\w[-+_=/]1")
        private val regexT2 = Regex("\\w[-+_=/]2")
        private val regexT3 = Regex("\\w[-+_=/]3")

        fun parse(convention: String): SettlementConvention {
            val value = convention.toUpperCase().trim()
            return when {
                regexT0.matches(value) -> T_0
                regexT1.matches(value) -> T_1
                regexT2.matches(value) -> T_2
                regexT3.matches(value) -> T_3
                else -> throw IllegalArgumentException("Bad SettlementConvention: '$convention'")
            }
        }
    }
}
*/