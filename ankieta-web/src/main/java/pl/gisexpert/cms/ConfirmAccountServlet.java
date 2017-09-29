package pl.gisexpert.cms;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.AccountStatus;
import pl.gisexpert.rest.util.producer.qualifier.RESTI18n;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

@WebServlet("/rest/auth/confirm")
public class ConfirmAccountServlet extends HttpServlet {

    @Inject
    private AccountRepository accountRepository;

    @RESTI18n
    private ResourceBundle i18n = ResourceBundle.getBundle("pl.gisexpert.i18n.Text");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String confirmationToken = request.getParameter("confirmationCode");

        Account account = accountRepository.findByToken(confirmationToken);

        if (!account.getAccountConfirmation().getToken().equals(confirmationToken)) {
            request.setAttribute("message", "Weryfikacja konta nie powiodła się. Prosimy o kontakt z administratorem.");
            request.getRequestDispatcher("/WEB-INF/confirmation.jsp").forward(request, response);
        }

        account.setAccountConfirmation(null);
        account.setAccountStatus(AccountStatus.CONFIRMED);
        accountRepository.edit(account);

        String subject = i18n.getString("account.confirm.adminemailsubject");
        String emailText =new StringBuilder().append("Użytkownik: ")
                .append(account.getUsername()).append(" prosi o weryfikację danych przez administratora.").toString();

        request.setAttribute("message", "Konto zostało zweryfikowane. " +
                "Administrator systemu został poproszony o zatwierdzenie twojego konta.");
        request.getRequestDispatcher("/WEB-INF/confirmation.jsp").forward(request, response);
    }

}
