package Klub;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;

public class ValidationUtil {
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    // Because fuck regex, stupid shit
    private static final String[] upperChar = "A Ą B C Ć D E Ę F G H I J K L Ł M N Ń O Ó P R S Ś T U W Y Z Ź Ż".split(" ");
    private static final String[] lowerChar = "a ą b c ć d e ę f g h i j k l ł m n ń o ó p r s ś t u w y z ź ż".split(" ");
    private static final String[] digits = "0 1 2 3 4 5 6 7 8 9".split(" ");
    private static final String[] specialCharacters = "! @ # & ( ) – [ { } ]: ; ' , ? / * ~ $ ^ + = < > -".split(" ");


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
public static void addValidation(MFXTextField textField, Label label, String validationType) {

    Constraint emptyConstraint = Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Pole nie może być puste!")
            .setCondition(Bindings.createBooleanBinding(
                    () -> !textField.getText().isEmpty(),
                    textField.textProperty()
            ))
            .get();

    Constraint lengthConstraint = Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Tekst musi być krótszy niż 30 znaków")
            .setCondition(textField.textProperty().length().lessThan(30))
            .get();


            Constraint upperCaseFirstLetterConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Pierwsza litera musi być DUŻA")
                .setCondition(Bindings.createBooleanBinding(
                        () -> !textField.getText().isEmpty() && Character.isUpperCase(textField.getText().charAt(0)),
                        textField.textProperty()
                ))
                .get();
    Constraint yearConstraint = Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Proszę podać cztery cyfry")
            .setCondition(Bindings.createBooleanBinding(
                            () ->  containsAny(textField.getText(), "", digits)
                            ))
            .setCondition(textField.textProperty().length().isEqualTo(4))
            .get();

    Constraint pesel = Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Proszę podać jedenaście cyfr")
            .setCondition(textField.textProperty().length().isEqualTo(11))
            .get();

    Constraint email = Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Proszę podać prawidłowy e-mail")
            .setCondition( Bindings.createBooleanBinding(()->{
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
                Matcher matcher = pattern.matcher(textField.getText());
                System.out.println(matcher.matches());
                return matcher.matches();
            }))
            .get();




    Constraint nr_fab = Constraint.Builder.build()
            .setSeverity(Severity.ERROR)
            .setMessage("Prosze podać wilkie litery i cyfry")
            .setCondition(
                    Bindings.createBooleanBinding(
                            () ->textField.getText().matches("(?!^[A-Z]+\\d+$)")
            ))
            .get();


    textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue) {
            label.setVisible(false);
            textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        }
    });

    textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
        if (oldValue && !newValue) {
            List<Constraint> constraints = textField.validate();
            if (!constraints.isEmpty()) {
                System.out.println(textField.getValidator().validateToString());
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                label.setText(constraints.get(0).getMessage());
                label.setVisible(true);
            }
        }
    });
         if (validationType.equals("textField")) {
            textField.getValidator()
                    .constraint(emptyConstraint)
                    .constraint(upperCaseFirstLetterConstraint)
                    .constraint(lengthConstraint);
        }
         if (validationType.equals("year")) {
         textField.getValidator()
                 .constraint(emptyConstraint)
                 .constraint(yearConstraint);
        }
         if (validationType.equals("nr_fab")) {
             textField.getValidator()
                     .constraint(emptyConstraint)
                     .constraint(nr_fab);
        }
         if (validationType.equals("empty")) {
             textField.getValidator()
                     .constraint(emptyConstraint);
        }

         if(validationType.equals("pesel")){
             textField.getValidator().constraint(emptyConstraint).constraint(pesel);

         }
         if(validationType.equals("email")){
             textField.getValidator().constraint(emptyConstraint);

             textField.getValidator().constraint(email);

         }
         if(validationType.equals("phone")){
             textField.getValidator().constraint(emptyConstraint);
         }


}

}
