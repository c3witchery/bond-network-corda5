package com.r3.developers.csdetemplate.digitalasset

import com.r3.developers.csdetemplate.utxoexample.states.ProspectusState
import net.corda.v5.application.flows.ClientRequestBody
import net.corda.v5.application.flows.ClientStartableFlow
import net.corda.v5.application.flows.CordaInject
import net.corda.v5.application.flows.InitiatingFlow
import net.corda.v5.application.marshalling.JsonMarshallingService
import net.corda.v5.application.membership.MemberLookup
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.ledger.utxo.UtxoLedgerService
import org.slf4j.LoggerFactory
import java.util.Date

data class ViewProspectusResults(
    val isin: String,
    val issuer: String,
    val investmentBank: String,
    val notional: Long,
    val maturity: Date,
    val denomination: Int?,
    )
class ViewProspectusFlow : ClientStartableFlow {
    private companion object {
        val log= LoggerFactory.getLogger(this::class.java.enclosingClass)
    }

    @CordaInject
    lateinit var jsonMarshallingService: JsonMarshallingService

    @CordaInject
    lateinit var ledgerService: UtxoLedgerService

    @CordaInject
    lateinit var memberLookup: MemberLookup

    @Suspendable
    override fun call(requestBody: ClientRequestBody): String {
        log.info("ViewProspectusFlow.call() called")
        val queryingMember = memberLookup.myInfo()
        val states = ledgerService.findUnconsumedStatesByType(ProspectusState::class.java)
            .filter { prospectus ->
            prospectus.state.contractState.issuer== queryingMember.ledgerKeys.first()
            }

        val results = states.map {
            ViewProspectusResults(
                it.state.contractState.isin,
                it.state.contractState.issuer.toString(),
                it.state.contractState.investmentBank.toString(),
                it.state.contractState.notional,
                it.state.contractState.maturity,
                it.state.contractState.denomination)
        }

        return jsonMarshallingService.format(results)
    }
}
