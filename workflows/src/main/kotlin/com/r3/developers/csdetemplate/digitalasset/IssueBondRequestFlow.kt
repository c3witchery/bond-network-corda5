package com.r3.developers.csdetemplate.digitalasset

import com.r3.developers.csdetemplate.utxoexample.contracts.BondContract
import com.r3.developers.csdetemplate.utxoexample.states.BondState
import com.r3.developers.csdetemplate.utxoexample.states.ProspectusState
import net.corda.v5.application.flows.*
import net.corda.v5.application.messaging.FlowSession
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.base.exceptions.CordaRuntimeException
import java.security.PublicKey
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.reflect.jvm.internal.impl.resolve.constants.NullValue


data class IssueBondRequest(
                            val issuer: String,
                            val investmentBank: String,
                            val maturity: Date,
                            val percentage: Int,
                            val denomination: Int?,
                            val issuanceDate: Date,
                            val settlementDate: Date,
                            val country: String
)


@InitiatingFlow(protocol = "issue-bond-flow")
class IssueBondRequestFlow(): AbstractFlow(), ClientStartableFlow {
    companion object
    {
        private const val RANGE = 36 * 36 * 36
        private const val RADIX = 36
    }
    private fun generateRandomIsin(prospectusData: IssueBondRequest): String {
        val prefix = "${prospectusData.country}XX".substring(0, 2)
        val random = Random()
        val buffer = StringBuilder("$prefix-")
        for (i in 1..3) {
            val r = (random.nextInt(RANGE) + RANGE).toString(RADIX)
            val l = r.length
            buffer.append(r.substring(l - 3, l))
        }
        buffer.append("-0")

        return buffer.toString().uppercase(Locale.getDefault())
    }
    @Suspendable
    override fun call(requestBody: ClientRequestBody): String {
        logger.info("${this::class.java.enclosingClass}.call() called")
        try {
            val myInfo = memberLookup.myInfo()
            val flowArgs = requestBody.getRequestBodyAs(json, IssueBondRequest::class.java)
            val isin = generateRandomIsin(flowArgs)
            val prospectusState = ledgerService.findUnconsumedStatesByType(ProspectusState::class.java).filter { prospectus ->
                prospectus.state.contractState.issuer == myInfo.ledgerKeys.first()
            }.first()
            if (prospectusState == null){logger.info("no prospectuses")}
            val issuanceDate = LocalDate.now()
            val defaultZoneId = ZoneId.systemDefault()
            val issuanceDateFinal = Date.from(issuanceDate.atStartOfDay(defaultZoneId).toInstant())
            val notional = prospectusState.state.contractState.notional
            val percentage = flowArgs.percentage
            val settlementDate = flowArgs.settlementDate
            val payment = (notional * percentage)
            val issuedBondState = BondState(
                isin = isin,
                issuer = myInfo.ledgerKeys.first(),
                investmentBank = prospectusState.state.contractState.investmentBank,
                notional = prospectusState.state.contractState.notional,
                maturity = prospectusState.state.contractState.maturity,
                settlementDate = settlementDate,
                payment = payment,
                denomination = flowArgs.denomination ?: 1,
                issuanceDate = issuanceDateFinal,
                participants = prospectusState.state.contractState.participants
            )

            val notary = notaryLookup.notaryServices.single()
            val signatories = mutableListOf<PublicKey>(myInfo.ledgerKeys.first()).union(prospectusState.state.contractState.participants)

            val txBuilder = ledgerService.createTransactionBuilder()
                .setNotary(notary.name)
                .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                .addOutputState(issuedBondState)
                .addCommand(BondContract.IssueBond())
                .addSignatories(signatories)

            // List to hold all the flow sessions
            val sessions = mutableListOf<FlowSession>()

            // Initiate a flow session with each participant
            for (participant in prospectusState.state.contractState.participants) {
                //logger.warn("participant: $participant")
                val node = memberLookup.lookup(participant)
                if (node != null) {
                    logger.warn("flow session initiated for ${node.name}")
                    sessions.add(flowMessaging.initiateFlow(node.name))
                }
            }


            val signedTransaction = txBuilder.toSignedTransaction()

            // Sign the transaction and collect signatures from all participants
            val finalizedSignedTransaction = ledgerService.finalize(signedTransaction, sessions)

            return finalizedSignedTransaction.transaction.id.toString().also {
                logger.info("Successful ${signedTransaction.commands.first()} with response: $it")
            }
        }
        catch (e: Exception) {
            logger.warn("create bond failed")
            throw e
        }
    }
}

@InitiatedBy(protocol = "issue-bond-flow")
class FinalizeCreateProductResponderFlow: AbstractFlow(), ResponderFlow {

    @Suspendable
    override fun call(session: FlowSession) {
        logger.info("${this::class.java.enclosingClass}.call() called")

        try {
            val finalizedSignedTransaction = ledgerService.receiveFinality(session) { ledgerTransaction ->
                val state = ledgerTransaction.getOutputStates(BondState::class.java).singleOrNull() ?:
                throw CordaRuntimeException("Failed verification - transaction did not have exactly one output Product.")

                logger.info("Verified the transaction- ${ledgerTransaction.id}")
            }
            logger.info("Finished create bond responder flow - ${finalizedSignedTransaction.transaction.id}")
        }
        catch (e: Exception) {
            logger.warn("Create Bond responder flow failed with exception", e)
            throw e
        }
    }
}

