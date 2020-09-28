package no.ntnu.backend.pentbrukt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("new-listing")
public class NewListingController {

    @GetMapping("index")
    public String listing(){
        return "/new-listing/index";
    }

}
