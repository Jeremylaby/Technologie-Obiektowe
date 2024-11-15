package command;

import model.Account;
import model.Transaction;

import java.util.List;

public class RemoveTransactionsCommand implements Command {
    private final List<Transaction> transactions;
    private final Account account;
    public RemoveTransactionsCommand(List<Transaction> transactions, Account account) {
        this.transactions = transactions;
        this.account = account;
    }
    @Override
    public void execute() {
        for(Transaction transaction : transactions) {
            account.removeTransaction(transaction);
        }
    }

    @Override
    public void undo() {
        for(Transaction transaction : transactions) {
            account.addTransaction(transaction);
        }
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public String getName() {
        return transactions.size() + " transactions removed.";
    }
}
