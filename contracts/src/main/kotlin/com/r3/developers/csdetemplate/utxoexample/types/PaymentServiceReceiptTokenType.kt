package com.r3.developers.csdetemplate.utxoexample.types
/*
import com.r3.developers.csdetemplate.utxoexample.types.DigitalAssetTokenType
import net.corda.core.contracts.Amount
import net.corda.core.identity.Party
import java.util.*

/**
 * Records the details of a payment processed by the Payment Service.
 * The buyer would create a FungibleToken of this type and then
 * use the Trade Offer protocol to exchange the token with the seller. The
 * seller's assets can only be unlocked by the buyer once the payment has
 * been successfully processed by the Payment Service.
 *
 * @property identifier Random UUID
 * @property issuer Buyer's party
 * @property issuanceId Asset version
 * @property paymentAgency Payment Service agent
 * @property amount Amount to be paid
 * @property debtor Payer's account id
 * @property creditor Payee's account id
 * @property externalRef External reference of payment
 */
class PaymentServiceReceiptTokenType(
    identifier: String,
    issuer: Party,
    issuanceId: UUID,
    paymentAgency: Party,
    val amount: Amount<Currency>,
    val debtor: String,
    val creditor: String,
    val externalRef: String
) : DigitalAssetTokenType(identifier, issuer, issuanceId, paymentAgency) {

    override fun toString(): String {
        return "PaymentServiceReceiptTokenType(" +
            "'$identifier', " +
            "'$issuer', " +
            "'$issuanceId', " +
            "'$paymentAgency' " +
            "'$amount' " +
            "'$debtor' " +
            "'$creditor' " +
            "'$externalRef')"
    }
}
*/