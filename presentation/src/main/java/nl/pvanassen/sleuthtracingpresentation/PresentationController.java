package nl.pvanassen.sleuthtracingpresentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
class PresentationController {

    private final int pages = 7;

    @NewSpan("creating model")
    @RequestMapping(value = "/sheets/{name}.html", method = RequestMethod.GET)
    public String sheets(@PathVariable(value = "name") String name,
                         final Model model) {
        int page = Integer.parseInt(name);
        if (page > pages) {
            log.error("Cannot find sheet {}", name);
            throw new NotFoundException();
        }
        log.info("Loading sheet {}", name);
        final String prevPage;
        final String nextPage;
        if (page == 1) {
            prevPage = name;
            nextPage = String.format("%02d", page + 1);
        }
        else if (page == pages) {
            prevPage = String.format("%02d", page - 1);
            nextPage = name;
        }
        else {
            prevPage = String.format("%02d", page - 1);
            nextPage = String.format("%02d", page + 1);
        }
        model.addAttribute("page", page);
        model.addAttribute("template", name);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("prevPage", prevPage);
        return "/index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(final Model model) {
        return sheets("01", model);
    }


}
