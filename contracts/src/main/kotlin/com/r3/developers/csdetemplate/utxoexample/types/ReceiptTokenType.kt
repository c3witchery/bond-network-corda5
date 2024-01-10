package com.r3.developers.csdetemplate.utxoexample.types
/*
import com.r3.developers.csdetemplate.utxoexample.types.DigitalAssetTokenType
import net.corda.core.contracts.Amount
import net.corda.core.identity.Party
import java.util.*

/**
 * Records the receipt of a payment for a digital asset.
 *
 * Receipt tokens and the asset tokens can be mutually settled on the ledger,
 * as long as the [issuanceId] matches the asset and the value of the
 * receipt equals the value of assets being redeemed.
 *
 * @property identifier Asset identifier
 * @property issuer Asset issuer
 * @property issuanceId Asset version
 * @property paymentAgency Asset payment agency
 * @property paymentName Name of payment of this receipt
 * @property receiptDenomination Value of a single receipt (notionally $1)
 * @property assetDenomination Value of a single asset
 */
class ReceiptTokenType(
    identifier: String,
    issuer: Party,
    issuanceId: UUID,
    paymentAgency: Party,
    paymentName: String,
    val receiptDenomination: Amount<Currency>,
    val assetDenomination: Amount<Currency>
) : DigitalAssetTokenType(identifier, issuer, issuanceId, paymentAgency) {
    override val paymentName = paymentName.toLowerCase()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ReceiptTokenType

        if (paymentName != other.paymentName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + paymentName.hashCode()
        return result
    }

    override fun toString(): String {
        return "ReceiptTokenType(" +
            "'$identifier', " +
            "'$issuer', " +
            "'$issuanceId', " +
            "'$paymentAgency', " +
            "'$paymentName', " +
            "'$receiptDenomination', " +
            "'$assetDenomination')"
    }
}
*/