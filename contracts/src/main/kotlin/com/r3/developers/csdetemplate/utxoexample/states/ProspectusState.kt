package com.r3.developers.csdetemplate.utxoexample.states

import com.r3.developers.csdetemplate.utxoexample.contracts.ProspectusContract
import net.corda.v5.ledger.utxo.BelongsToContract
import net.corda.v5.ledger.utxo.ContractState
import java.security.PublicKey
import java.util.Date

@BelongsToContract(ProspectusContract::class)
class ProspectusState(
    val isin: String,
    val issuer: PublicKey,
    val investmentBank: PublicKey,
    val notional: Long,
    val maturity: Date,
    val denomination: Int?,
    private val participants: List<PublicKey>) : ContractState {
        override fun getParticipants(): List<PublicKey> {
            return listOf(issuer, investmentBank)
    }
}
