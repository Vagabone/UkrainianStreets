package ua.skarb.ukrainianStreets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.skarb.ukrainianStreets.service.interfaces.LocalityService;

@Controller()
@RequestMapping(path = "/localities")
public class LocalityController {

    private final LocalityService localityService;

    @Autowired
    public LocalityController(LocalityService localityService) {
        this.localityService = localityService;
    }

    @RequestMapping(path = "/{regionId}",method = RequestMethod.GET)
    public String getLocalitiesByRegionId(@PathVariable Long regionId, Model model){
        model.addAttribute("localitiesList",localityService.getLocalitiesByRegionId(regionId));
        return "test :: localitiesList";
    }
}
