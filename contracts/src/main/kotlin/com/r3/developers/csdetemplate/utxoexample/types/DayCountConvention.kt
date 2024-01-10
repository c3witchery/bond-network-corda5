package com.r3.developers.csdetemplate.utxoexample.types
/*
import net.corda.v5.base.annotations.CordaSerializable

/**
 * Defines the day count conventions
 */
@CordaSerializable
enum class DayCountConvention(val display: String) {
    D30_360("30/360"),
    D30_365("30/365"),
    ACT_360("ACT/360"),
    ACT_365("ACT/365"),
    ACT_ACT("ACT/ACT");

    companion object {
        private val regexD30T360 = Regex(".?30[-_/]360")
        private val regexD30T365 = Regex(".?30[-_/]365")
        private val regexACTT360 = Regex("ACT[-_/]360")
        private val regexACTT365 = Regex("ACT[-_/]365")
        private val regexACTTACT = Regex("ACT[-_/]ACT")

        fun parse(convention: String): DayCountConvention {
            val value = convention.toUpperCase().trim()
            return when {
                regexD30T360.matches(value) -> D30_360
                regexD30T365.matches(value) -> D30_365
                regexACTT360.matches(value) -> ACT_360
                regexACTT365.matches(value) -> ACT_365
                regexACTTACT.matches(value) -> ACT_ACT
                else -> throw IllegalArgumentException("Bad DayCountConvention: '$convention'")
            }
        }
    }
}
*/