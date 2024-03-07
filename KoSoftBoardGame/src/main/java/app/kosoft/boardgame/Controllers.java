package app.kosoft.boardgame;

import app.kosoft.boardgame.data.RegistrationData;
import app.kosoft.boardgame.data.ValidationFailure;
import app.kosoft.boardgame.db.SurrealDB;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * A template alapú oldalak vezérlője.
 */
@Controller
class Controllers {
    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/register")
    String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        RegistrationData data = new RegistrationData(email, username, password);

        ValidationFailure failure = SurrealDB.getInstance().tryRegister(data);
        if (failure == null)
            return "redirect:/login?registered";
        else
            return "redirect:/register?" + failure.toString().toLowerCase();
    }

}
