package us.group14.backend.registration;

import com.leonsemmens.securitycourse.constants.ApiResponse;
import com.leonsemmens.securitycourse.registration.token.ConfirmationToken;
import com.leonsemmens.securitycourse.registration.token.ConfirmationTokenService;
import com.leonsemmens.securitycourse.user.User;
import com.leonsemmens.securitycourse.user.UserRole;
import com.leonsemmens.securitycourse.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public ResponseEntity<String> register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            return ApiResponse.INVALID_EMAIL.toResponse();
        }

        ResponseEntity<String> token = userService.register(new User(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
        ));

        return token;
    }

    @Transactional
    public ResponseEntity<String> confirmToken(String token) {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenService.getToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            return ApiResponse.CONFIRMATION_TOKEN_NOT_FOUND.toResponse();
        }

        ConfirmationToken confirmationToken = confirmationTokenOptional.get();
        if (confirmationToken.getConfirmedAt() != null) {
            return ApiResponse.ALREADY_CONFIRMED_EMAIL.toResponse();
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            return ApiResponse.CONFIRMATION_TOKEN_EXPIRED.toResponse();
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enable(confirmationToken.getUser());
        return ApiResponse.OK.toResponse();
    }

//    private String buildEmail(String name, String link) {
//        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
//                "\n" +
//                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
//                "\n" +
//                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
//                "        \n" +
//                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
//                "          <tbody><tr>\n" +
//                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
//                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
//                "                  <tbody><tr>\n" +
//                "                    <td style=\"padding-left:10px\">\n" +
//                "                  \n" +
//                "                    </td>\n" +
//                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
//                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
//                "                    </td>\n" +
//                "                  </tr>\n" +
//                "                </tbody></table>\n" +
//                "              </a>\n" +
//                "            </td>\n" +
//                "          </tr>\n" +
//                "        </tbody></table>\n" +
//                "        \n" +
//                "      </td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table>\n" +
//                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
//                "      <td>\n" +
//                "        \n" +
//                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
//                "                  <tbody><tr>\n" +
//                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
//                "                  </tr>\n" +
//                "                </tbody></table>\n" +
//                "        \n" +
//                "      </td>\n" +
//                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table>\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
//                "    <tbody><tr>\n" +
//                "      <td height=\"30\"><br></td>\n" +
//                "    </tr>\n" +
//                "    <tr>\n" +
//                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
//                "        \n" +
//                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
//                "        \n" +
//                "      </td>\n" +
//                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
//                "    </tr>\n" +
//                "    <tr>\n" +
//                "      <td height=\"30\"><br></td>\n" +
//                "    </tr>\n" +
//                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
//                "\n" +
//                "</div></div>";
//    }
}
