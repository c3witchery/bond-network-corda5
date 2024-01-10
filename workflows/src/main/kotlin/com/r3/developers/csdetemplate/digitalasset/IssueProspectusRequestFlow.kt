package com.r3.developers.csdetemplate.digitalasset

import com.r3.developers.csdetemplate.utxoexample.contracts.ProspectusContract
import com.r3.developers.csdetemplate.utxoexample.states.ProspectusState
import net.corda.v5.application.flows.*
import net.corda.v5.application.messaging.FlowSession
import net.corda.v5.base.annotations.Suspendable
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.base.types.MemberX500Name
import java.security.PublicKey
import java.time.Duration
import java.time.Instant
import java.util.*

/**
 * Creates a [ProspectusState]
 */
data class IssueProspectusRequest(
    val investmentBank: String,
    val notional: Long,
    val maturity: Date,
    val denomination: Int?,
    val country: String
)

@InitiatingFlow(protocol = "finalize-issue-prospectus-protocol")
class IssueProspectusRequestFlow() : ClientStartableFlow, AbstractFlow() {
    companion object
    {
        private const val RANGE = 36 * 36 * 36
        private const val RADIX = 36
    }
    private fun generateRandomIsin(prospectusData: IssueProspectusRequest): String {
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
        try{
            val flowArgs = requestBody.getRequestBodyAs(json, IssueProspectusRequest::class.java)
            val ourIdentity = memberLookup.myInfo().ledgerKeys.first()
            val investmentBank = memberLookup.lookup(MemberX500Name.parse(flowArgs.investmentBank))?:
            throw CordaRuntimeException("MemberLookup can't find investment bank specified in flow arguments.")
            val isin = generateRandomIsin(flowArgs)
            val prospectusData = ProspectusState(
                isin =isin,
                issuer = ourIdentity,
                investmentBank.ledgerKeys.first(),
                flowArgs.notional,
                flowArgs.maturity,
                flowArgs.denomination,
                participants = listOf(ourIdentity, investmentBank.ledgerKeys.first()))

            logger.info("Received ISIN $isin from investment bank")


            val notary = notaryLookup.notaryServices.single()
            val signatories = mutableListOf<PublicKey>(ourIdentity).union(prospectusData.participants)
            val txBuilder = ledgerService.createTransactionBuilder()
                .setNotary(notary.name)
                .setTimeWindowBetween(Instant.now(), Instant.now().plusMillis(Duration.ofDays(1).toMillis()))
                .addOutputState(prospectusData)
                .addCommand(ProspectusContract.IssueProspectus())
                .addSignatories(signatories)

            val signedTransaction = txBuilder.toSignedTransaction()
            val session = flowMessaging.initiateFlow(investmentBank.name)
            val finalizedSignedTransaction = ledgerService.finalize(
                signedTransaction,
                listOf(session))
            return finalizedSignedTransaction.transaction.id.toString().also {
                logger.info("Successful ${signedTransaction.commands.first()} with response: $it")
            }
        }
        catch (e: Exception) {
            logger.warn("Failed to process issue prospectus for request body '$requestBody' with exception: '${e.message}'")
            throw e
        }
    }
}

@InitiatedBy(protocol = "finalize-issue-prospectus-protocol")
class FinalizeIssueProspectusResponderFlow: AbstractFlow(), ResponderFlow {

    @Suspendable
    override fun call(session: FlowSession) {
        logger.info("${this::class.java.enclosingClass}.call() called")

        try {
            val finalizedSignedTransaction = ledgerService.receiveFinality(session) { ledgerTransaction ->
                val state = ledgerTransaction.getOutputStates(ProspectusState::class.java).singleOrNull() ?:
                throw CordaRuntimeException("Failed verification - transaction did not have exactly one output Prospectus.")

                logger.info("Verified the transaction- ${ledgerTransaction.id}")
            }
            logger.info("Finished issue prospectus responder flow - ${finalizedSignedTransaction.transaction.id}")
        }
        catch (e: Exception) {
            logger.warn("Issue Prospectus responder flow failed with exception", e)
            throw e
        }
    }
}