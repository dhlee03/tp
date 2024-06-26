package seedu.address.logic.parser.alias;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.AddCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddCommand object that adds a
 * participant to the address book.
 */
public class AddParticipantAlias extends Alias<AddCommand> {
    public static final String ALIAS_WORD = "ap";

    public static final String MESSAGE_USAGE = ALIAS_WORD + ": Adds a participant to the contact list. \n"
            + "Parameters: " + PREFIX_NAME + "NAME " + PREFIX_PHONE + "PHONE " + PREFIX_EMAIL + "EMAIL " + "["
            + PREFIX_GROUP + "GROUP]\n" + "Example: " + ALIAS_WORD + " " + PREFIX_NAME + "John Doe " + PREFIX_PHONE
            + "98765432 " + PREFIX_EMAIL + "johndoe@example.com ";

    private String toAddCommandInput(String input) {
        String paramCategory = PREFIX_CATEGORY + "participant";
        return input + " " + paramCategory;
    }

    @Override
    public AddCommand parse(String input) throws ParseException {
        String addCommandInput = toAddCommandInput(input);
        try {
            return new AddCommandParser().parse(addCommandInput);
        } catch (ParseException e) {
            // Override add command usage with own usage. Temporary workaround for now
            String addCommandUsage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
            if (e.getMessage().equals(addCommandUsage)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
            } else {
                throw e;
            }
        }
    }
}
