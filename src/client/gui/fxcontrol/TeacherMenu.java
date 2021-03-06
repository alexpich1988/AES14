package client.gui.fxcontrol;

import client.control.TeacherControl;
import com.Contract;
import com.data.Message;
import com.data.Question;
import com.style.icons.FontAwesome;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.util.Optional;

public class TeacherMenu {


    public TeacherControl teacherControl = TeacherControl.getInstance();
    public AnchorPane questionPane;
    public ListView questionList;
    public Label plusIcon;
    public Label editIcon;
    public Label delIcon;
    public Button editBtn;
    public Button deleteBtn;
    public Label eyeIcon;
    public Label examsIcon;
    public Label questionsIcon;


    public void initialize() {
        plusIcon.setFont(FontAwesome.getFont(FontAwesome.SOLID));
        editIcon.setFont(FontAwesome.getFont(FontAwesome.SOLID));
        delIcon.setFont(FontAwesome.getFont(FontAwesome.SOLID));
        eyeIcon.setFont(FontAwesome.getFont(FontAwesome.SOLID));
        examsIcon.setFont(FontAwesome.getFont(FontAwesome.SOLID));
        questionsIcon.setFont(FontAwesome.getFont(FontAwesome.SOLID));

        questionList.setItems(TeacherControl.getInstance().questions);
        ValidationSupport support = new ValidationSupport();
        support.registerValidator(questionList, Validator.createEmptyValidator("Subject Selection Is Required!"));
        editBtn.disableProperty().bind(support.invalidProperty());
        deleteBtn.disableProperty().bind(support.invalidProperty());
    }


    public void openSingleQuestion(Question question) {
        try {
            SingleQuestion.openDialog(new Stage(), question);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openNewQuestionDialog(ActionEvent actionEvent) {
        Question question = new Question("", new String[]{"", "", "", ""}, 0, null, teacherControl.teacher);
        openSingleQuestion(question);
        System.out.println(question);
        if (question.getQuestionText().equals(""))
            try {
                teacherControl.client.sendToServer(new Message(Contract.CREATE_QUESTION, question));
            } catch (IOException e) {
                e.printStackTrace();
            }
        //update questions
    }


    public void openEditQuestionDialog(ActionEvent actionEvent) {
        for (Object q :
                questionList.getSelectionModel().getSelectedItems()) {
            if (((Question) q).getAuthor().getUsername().equals(teacherControl.teacher.getUsername())) {
                openSingleQuestion((Question) q);
                try {
                    teacherControl.client.sendToServer(new Message(Contract.UPDATE_QUESTION, q));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Can't Edit Question!");
                alert.setContentText("You can't edit questions you didn't write!");
                alert.showAndWait();
            }

        }


    }

    public void deleteQuestion(ActionEvent actionEvent) {
        //display warning dialog
        for (Object q :
                questionList.getSelectionModel().getSelectedItems()) {
            if (((Question) q).getAuthor().getUsername().equals(teacherControl.teacher.getUsername())) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Wait!");
                alert.setHeaderText("Are You Sure?");
                alert.setContentText("You can't restore the questions you delete! " +
                        "\nAre you sure you wan't to delete this question?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    //send question to deletion
                    try {
                        teacherControl.client.sendToServer(new Message(Contract.DELETE_QUESTION, q));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Can't Edit Question!");
                alert.setContentText("You can't edit questions you didn't write!");
                alert.showAndWait();
            }
        }
    }

    public void openView(ActionEvent actionEvent) {
        for (Object q :
                questionList.getSelectionModel().getSelectedItems()) {
            try {
                QuestionView.openWindow(new Stage(), (Question) q);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
