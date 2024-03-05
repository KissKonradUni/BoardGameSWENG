package app.kosoft.boardgame;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * A template alapú oldalak vezérlője.
 */
@Controller
class TemplateControllers {
    @GetMapping("/")
    String index() {
        return "index";
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }
}
