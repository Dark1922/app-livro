package validator;

import javax.swing.*;
import java.util.Date;

public class LivroValidator {

    public static boolean validarCampos(JTextField txtTitulo, JTextField txtAutores, JFormattedTextField txtDataPublicacao, JTextField txtIsbn, JTextField txtEditora, JDialog parent) {
        if (txtTitulo.getText().isEmpty()) {
            showError(parent, "O campo Título é obrigatório!");
            return false;
        }
        if (txtAutores.getText().isEmpty()) {
            showError(parent, "O campo Autores é obrigatório!");
            return false;
        }
        if (txtIsbn.getText().isEmpty()) {
            showError(parent, "O campo ISBN é obrigatório!");
            return false;
        }
        if (txtEditora.getText().isEmpty()) {
            showError(parent, "O campo Editora é obrigatório!");
            return false;
        }

        // Validação da data
        if (parseDate(txtDataPublicacao.getText()) == null) {
            showError(parent, "Data de publicação inválida! Use o formato dd/MM/yyyy.");
            return false;
        }

        // Validação do ISBN
        String isbn = txtIsbn.getText();
        if (isbn.length() != 13 || !isbn.matches("\\d{13}")) {
            showError(parent, "O ISBN deve conter exatamente 13 dígitos!");
            return false;
        }

        return true;
    }

    private static void showError(JDialog parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    private static Date parseDate(String dateText) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            return sdf.parse(dateText);
        } catch (java.text.ParseException e) {
            return null;
        }
    }
}