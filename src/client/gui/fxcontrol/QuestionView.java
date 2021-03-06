package client.gui.fxcontrol;

import com.Contract;
import com.data.Question;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class QuestionView {
    public Label subjectLbl;
    public Label questionText;
    public Label qidLbl;
    public Label answer1Label;
    public Label answer2Label;
    public Label answer3Label;
    public Label answer4Label;
    public Label authorLbl;

    public static void openWindow(Stage stage, Question q) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SingleQuestion.class.getResource(Contract.clientFXML + "QuestionView.fxml"));
        Parent root = fxmlLoader.load();
        QuestionView dialogController = fxmlLoader.getController();
        dialogController.setQuestion(q);
        Scene scene = new Scene(root, 600, 500);
        scene.getStylesheets().add(QuestionView.class.getResource(Contract.css).toExternalForm());
        stage.setTitle("Question " + q.getQIDString());
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void initialize() {


    }

    private void setQuestion(Question q) {
        qidLbl.setText(q.getQIDString());
        subjectLbl.setText(q.getSubject().getSubjectName());
        questionText.setText(q.getQuestionText());
        authorLbl.setText("Written By: " + q.getAuthor().getFullName());
        answer1Label.setText(q.getAns(0));
        answer2Label.setText(q.getAns(1));
        answer3Label.setText(q.getAns(2));
        answer4Label.setText(q.getAns(3));
    }
}
