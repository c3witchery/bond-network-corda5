package com.r3.developers.csdetemplate.utxoexample.types
/*
import com.r3.corda.lib.tokens.contracts.types.TokenType
import net.corda.core.identity.Party
import java.util.*

/**
 * Base class for all digital asset token types.
 * All assets must have an [identifier], an [issuer], an [issuanceId] and a [paymentAgency].
 *
 * The [paymentAgency] is responsible for making payments on the asset as applicable,
 * such as coupon payment, or redemption payment. If not set this will be the [issuer].
 *
 * @property identifier Asset identifier
 * @param issuer Asset issuer
 * @property issuanceId Asset version
 * @property paymentAgency Agency responsible for payments on this asset
 */
open class DigitalAssetTokenType(
    val identifier: String,
    val issuer: Party,
    val issuanceId: UUID,
    val paymentAgency: Party = issuer
): TokenType(identifier,0){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as DigitalAssetTokenType

        if (identifier != other.identifier) return false
        if (issuer != other.issuer) return false
        if (issuanceId != other.issuanceId) return false
        if (paymentAgency != other.paymentAgency) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + identifier.hashCode()
        result = 31 * result + issuanceId.hashCode()
        return result
    }

    override fun toString(): String {
        return "DigitalAssetTokenType(" +
            "'$identifier', " +
            "'$issuer', " +
            "'$issuanceId', " +
            "'$paymentAgency')"
    }

    /**
     * Return the payment name of the token
     */
    open val paymentName: String = "none"
}
*/