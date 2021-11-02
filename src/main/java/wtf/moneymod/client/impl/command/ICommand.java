package wtf.moneymod.client.impl.command;

/**
 * @author cattyn
 * @since 11/02/21
 */

@FunctionalInterface
public interface ICommand {

    void execute(String[] args);

}
