package app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TapController {

    @RequestMapping("/mainpage")
   /* public String greeting(@RequestParam(value="name", required=false, defaultValue="Tuana") String name) {
        return "mainpage";
    }
    */
    public void start() {
        return;
    }

}
