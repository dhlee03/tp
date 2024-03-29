package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.UndoException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. " + "Parameters: "
            + PREFIX_NAME + "NAME " + PREFIX_PHONE + "PHONE " + PREFIX_EMAIL + "EMAIL " + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n" + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 " + PREFIX_EMAIL + "johnd@example.com " + PREFIX_ADDRESS
            + "311, Clementi Ave 2, #02-25 " + PREFIX_CATEGORY + "participant" + PREFIX_TAG + "friends " + PREFIX_TAG
            + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_SUCCESS_UNDO = "Person deleted: %1$s";
    public static final String MESSAGE_UNDO_NONEXISTENT_PERSON = "Undo failed:"
            + "Person does not exist in the address book";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        model.addCommand(this);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public CommandResult undo(Model model) throws UndoException {
        requireNonNull(model);

        if (!model.hasPerson(toAdd)) {
            throw new UndoException(MESSAGE_UNDO_NONEXISTENT_PERSON);
        }
        model.deletePerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS_UNDO, Messages.format(toAdd)));
    }

    @Override
    public CommandResult redo(Model model) throws UndoException, CommandException {
        return execute(model);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("toAdd", toAdd).toString();
    }
}
