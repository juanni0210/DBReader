package dbreader;

import static common.CommonGUIBuilder.createButton;
import static common.CommonGUIBuilder.createComboBox;
import static common.CommonGUIBuilder.createListView;
import static common.CommonGUIBuilder.createPasswordField;
import static common.CommonGUIBuilder.createTextField;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import jdbc.JDBCController;

public class ConnectDialog {

    /**
     * title of the Dialog
     */
    private static final String TITLE = "Connect To";
    /**
     * username used in the DB
     */
    private static final String USERNAME = "cst8288";
    /**
     * password used in the DB
     */
    private static final String PASSWORD = "8288";

    private Dialog<ButtonType> dialog;
    private JDBCController controller;
    private ComboBox<String> dbTypeCombo;
    private TextField hostText;
    private TextField portText;
    private TextField dbNameText;
    private TextField userText;
    private PasswordField passText;
    private ObservableList< Pair< String, String>> properties;

    private TextField keyText;
    private TextField valueText;

    public ConnectDialog(JDBCController controller) {
        //TODO save the argument of the constructor
        this.controller = controller;
        //TODO initialize properties variable using FXCollections.
        properties = FXCollections.observableArrayList();
    }

    public void init() {
        //TODO instantiate the dialog variable and call createGUI()
        dialog = new Dialog<ButtonType>();
        createGUI();
    }

    /**
     * this is a blocking method which will display the dialog. only returns after dialog is not visible anymore.
     * 
     * if cancel is pressed none of the data will be saved in controller.
     * 
     * @return true of cancel button was not pressed.
     */
    public boolean showAndWait() {
        //TODO Follow the sequence diagram
        //1.1 && 1.2
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().getButtonData().isCancelButton()) {
            //1.3
            controller.setDataBase(hostText.getText(), portText.getText(), dbNameText.getText());
            //1.4
            controller.addConnectionURLProperty(keyText.getText(), valueText.getText());
            //1.5
            controller.addConnectionURLProperty("useUnicode", "true");
            //1.6
            controller.setCredentials(userText.getText(), passText.getText());
            //1.7
            return true;
        }
        //1.8
        return false;
    }

    private void createGUI() {
        dialog.setTitle(TITLE);

        dbTypeCombo = createComboBox(FXCollections.observableArrayList("mysql"), "DB Type", 0);
        hostText = createTextField("localhost", "Host Name");
        portText = createTextField("3306", "Port Number");
        dbNameText = createTextField("redditreader", "DB Name");
        userText = createTextField(USERNAME, "Username");
        passText = createPasswordField(PASSWORD, "Password");
        keyText = createTextField("serverTimezone", "Key");
        valueText = createTextField("UTC", "Value");
        ListView<Pair<String, String>> propertiesList = createListView(properties, 165);

        Button addProperty = createButton("Add",
                e -> properties.add(new Pair<>(keyText.getText(), valueText.getText())));
        ButtonType connectButton = new ButtonType("Connect", ButtonData.OK_DONE);

        GridPane grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(3);
        grid.setPadding(new Insets(5, 5, 5, 5));

        grid.add(dbTypeCombo, 0, 0);
        grid.add(hostText, 0, 1);
        grid.add(portText, 0, 2);
        grid.add(dbNameText, 0, 3);
        grid.add(userText, 0, 4);
        grid.add(passText, 0, 5);
        grid.add(propertiesList, 1, 0, 2, 6);
        grid.add(keyText, 0, 7);
        grid.add(valueText, 1, 7);
        grid.add(addProperty, 2, 7);

        dialog.getDialogPane().getButtonTypes().addAll(connectButton, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);
    }
}
