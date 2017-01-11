package ua.skarb.ukrainianStreets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.skarb.ukrainianStreets.service.interfaces.StreetService;

@Controller
@RequestMapping(path = "/streets")
public class StreetController {

    private final StreetService streetService;

    @Autowired
    public StreetController(StreetService streetService) {
        this.streetService = streetService;
    }

    @RequestMapping(path = "/{localityId}", method = RequestMethod.GET)
    String getStreetsbyLocalityId(@PathVariable Long localityId, Model model){
        model.addAttribute("streetsList", streetService.getStreetsByLocalityId(localityId));
        return "test :: streetsList";
    }
}
