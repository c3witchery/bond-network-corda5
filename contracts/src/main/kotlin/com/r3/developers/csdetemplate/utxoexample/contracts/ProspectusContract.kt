package com.r3.developers.csdetemplate.utxoexample.contracts
import com.r3.developers.csdetemplate.utxoexample.states.ProspectusState
import net.corda.v5.base.exceptions.CordaRuntimeException
import net.corda.v5.ledger.utxo.Command
import net.corda.v5.ledger.utxo.Contract
import net.corda.v5.ledger.utxo.transaction.UtxoLedgerTransaction

class ProspectusContract: Contract {

    class IssueProspectus: Command

    override fun verify(transaction: UtxoLedgerTransaction) {
        val command = transaction.commands.firstOrNull { it is IssueProspectus }
            ?: throw CordaRuntimeException("Requires a single Prospectus command")

        when(command) {
            is IssueProspectus -> {
                "When command is Issue there should be no input states." using (transaction.inputContractStates.isEmpty())
                "When command is Issue there should be one and only one output state." using (transaction.outputContractStates.size == 1)


            }
            else -> {
                throw CordaRuntimeException("Command $command not allowed.")
            }
        }
    }

    // Helper function to allow writing constraints in the Corda 4 '"text" using (boolean)' style
    private infix fun String.using(expr: Boolean) {
        if (!expr) throw CordaRuntimeException("Failed requirement: $this")
    }

    // Helper function to allow writing constraints in '"text" using {lambda}' style where the last expression
    // in the lambda is a boolean.
    private infix fun String.using(expr: () -> Boolean) {
        if (!expr.invoke()) throw CordaRuntimeException("Failed requirement: $this")
    }
}