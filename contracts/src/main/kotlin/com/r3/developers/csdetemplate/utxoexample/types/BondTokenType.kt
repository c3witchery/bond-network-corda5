package com.r3.developers.csdetemplate.utxoexample.types
/*
import com.r3.developers.csdetemplate.utxoexample.types.DigitalAssetTokenType
import net.corda.core.contracts.Amount
import net.corda.core.identity.Party
import java.time.Instant
import java.util.*

/**
 * Records the bond token type. A bond holding is recorded as a fungible token
 * of a [BondTokenType], and a holder's position is the product of the
 * fungible quantity and the denomination. The [issuanceId] records the version
 * of the bond and is the linear id of the BondState.
 *
 * @property identifier Bond identifier
 * @property issuer Bond issuer
 * @property issuanceId Bond version
 * @property paymentAgency Bond payment agency
 * @property denomination Bonds smalled denomination
 * @property maturity When the bond matures
 * @property callable Whether the bond can be redeemed before maturity
 */
class BondTokenType(
    identifier: String,
    issuer: Party,
    issuanceId: UUID,
    paymentAgency: Party,
    val denomination: Amount<Currency>,
    val maturity: Instant,
    val callable: Boolean
): DigitalAssetTokenType(identifier,issuer, issuanceId, paymentAgency){
    override val paymentName = PAYMENT_NAME

    companion object {
        const val PAYMENT_NAME = "maturity"
    }

    override fun toString(): String {
        return "BondTokenType(" +
            "'$identifier', " +
            "'$issuer', " +
            "'$issuanceId', " +
            "'$paymentAgency', " +
            "'$denomination', " +
            "'$maturity', " +
            "$callable)"
    }
}
*/