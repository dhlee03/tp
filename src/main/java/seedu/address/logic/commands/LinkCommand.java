package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports all selected participants to the event.
 */
public class LinkCommand extends Command {

    public static final String COMMAND_WORD = "link";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Links all selected people to sponsors by "
            + "exporting into a csv file. "
            + "Parameters: one or multiple index (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_SUCCESS = "Exported all selected people";

    private static Logger logger = Logger.getLogger("LinkCommandLogger");
    private final List<Index> indexes;


    /**
     * Creates a LinkCommand to send information of the specified {@code Person} using a csv file.
     *
     * @param indexes the indexes of the selected people
     */
    public LinkCommand(Index[] indexes) {
        requireNonNull(indexes);
        this.indexes = Arrays.asList(indexes);
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        validateIndexes(indexes, lastShownList);

        Path filePath = Path.of("./selectedPeople/list.csv");
        createFile(filePath);
        writeToFile(filePath, model);

        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Validates the indexes of the selected people.
     *
     * @param indexes the indexes of the selected people
     * @param persons the list of all people
     * @throws CommandException if the indexes are invalid
     */
    private void validateIndexes(List<Index> indexes, List<Person> persons) throws CommandException {
        for (Index index : indexes) {
            boolean isNegative = index.getZeroBased() < 0;
            boolean isOver = index.getZeroBased() >= persons.size();
            if (isNegative || isOver) {
                //checks if the user input is a valid index
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }
        checkDuplicateIndexes(indexes);
    }

    /**
     * Checks if the user input has duplicate indexes.
     *
     * @param indexes all indexes of the selected people
     * @throws CommandException if there are duplicate indexes.
     */
    private void checkDuplicateIndexes(List<Index> indexes) throws CommandException {
        for (int i = 0; i < indexes.size(); i++) {
            for (int j = i + 1; j < indexes.size(); j++) {
                if (indexes.get(i).equals(indexes.get(j))) {
                    //checks if the user input has duplicate indexes
                    throw new CommandException(Messages.MESSAGE_DUPLICATE_INDEX);
                }
            }
        }
    }

    /**
     * Creates a empty csv file with header for the selected people.
     *
     * @param filePath the path of the csv file
     * @throws CommandException if there is an error creating the file
     */
    private void createFile(Path filePath) throws CommandException {
        logger.log(Level.INFO, "Creating csv file for selected people");
        String header = "Name, Phone, Email, Comment\n";

        try {
            FileUtil.createCsvFile(filePath, header);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error creating a csv file");
            throw new CommandException(Messages.MESSAGE_IO_ERROR);
        }
        logger.log(Level.INFO, "Created csv file for selected people");
    }

    /**
     * Writes the selected people to the csv file.
     *
     * @param filePath the path of the csv file
     * @param model    the model of the hacklink
     * @throws CommandException if there is an error writing to the file
     */
    private void writeToFile(Path filePath, Model model) throws CommandException {
        logger.log(Level.INFO, "Writing selected people to csv file");
        for (Index index : indexes) {
            Person person = model.getFilteredPersonList().get(index.getZeroBased());
            try {
                FileUtil.appendToFile(filePath, person.toCsvString());
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error writing to a csv file");
                throw new CommandException(Messages.MESSAGE_IO_ERROR);
            }
        }
        logger.log(Level.INFO, "Wrote selected people to csv file");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LinkCommand)) {
            return false;
        }

        LinkCommand otherLinkCommand = (LinkCommand) other;
        return indexes.equals(otherLinkCommand.indexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("indexes", indexes)
                .toString();
    }
}
