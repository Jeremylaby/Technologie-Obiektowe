package command;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CommandRegistry {

	private ObservableList<Command> commandUndoStack = FXCollections
			.observableArrayList();
	private ObservableList<Command> commandRedoStack = FXCollections
			.observableArrayList();

	public void executeCommand(Command command) {
		command.execute();
		commandUndoStack.add(command);
		commandRedoStack.clear();
	}

	public void redo() {
		if (!commandRedoStack.isEmpty()) {
			Command command = commandRedoStack.removeLast();
			commandUndoStack.add(command);
			command.redo();
		}
	}

	public void undo() {
		if (!commandUndoStack.isEmpty()) {
			Command command = commandUndoStack.removeLast();
			commandRedoStack.add(command);
			command.undo();
		}

	}

	public ObservableList<Command> getCommandStack() {
		return commandUndoStack;
	}
}
