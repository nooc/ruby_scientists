module yh.rubysci.spaceadventure {
    requires javafx.controls;
    requires javafx.fxml;


    opens yh.rubysci.spaceadventure to javafx.fxml;
    exports yh.rubysci.spaceadventure;
    exports yh.rubysci.spaceadventure.logic;
    opens yh.rubysci.spaceadventure.logic to javafx.fxml;
}