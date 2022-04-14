package by.kynca.estateWeb.controller;

import by.kynca.estateWeb.entity.*;
import by.kynca.estateWeb.entity.validatorMarks.BasicEstateValidate;
import by.kynca.estateWeb.entity.validatorMarks.FlatValidate;
import by.kynca.estateWeb.service.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estates")
public class RealEstateController {

    private final RealEstateService estateService;

    @Autowired
    public RealEstateController(RealEstateService estateService) {
        this.estateService = estateService;
    }


    @GetMapping("/")
    public String catalog(@RequestParam("page") Optional<Integer> page, @RequestParam("sort") Optional<String> sort, Model model) {

        List<RealEstate> catalog = estateService.findAll(page.orElse(0), sort.orElse("realEstateId"));
        setPaginatedCatalog(model, page.orElse(0), sort.orElse("realEstateId"), "", catalog, estateService.getAllPagesAmount());
        return "catalog";
    }


    @GetMapping("/{estateType}")
    public String specificCatalog(@RequestParam("page") Optional<Integer> page, @RequestParam("sort") Optional<String> sort,
                                  @PathVariable String estateType, Model model) {
        List<RealEstate> catalog = estateService.findAllByType(estateType, page.orElse(0), sort.orElse("realEstateId"));

        RealEstateType type = catalog.get(0).getRealEstateType();
        setPaginatedCatalog(model, page.orElse(0), sort.orElse("realEstateId"),
                type.getValue().toLowerCase() + "/", catalog, estateService.getByTypePagesAmount(type));
        return "catalog";
    }

    @GetMapping("/get/{id}")
    public String getRealEstateInfo(@PathVariable Long id, Model model) {
        RealEstate realEstate = estateService.findById(id);
        if (realEstate == null) {
            return "redirect:/estates/";
        }
        model.addAttribute("estate", realEstate);

        switch (realEstate.getRealEstateType()) {
            case FLAT -> model.addAttribute("file", "flat.jpg");
            case HOUSE -> model.addAttribute("file", "house.jpg");
            case LAND -> model.addAttribute("file", "land.jpg");
        }
        return "estatePage";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("estate", new RealEstate());
        return "newEstate";
    }

    @GetMapping("/newFlat")
    public String flatForm(Model model) {
        RealEstate flat = new RealEstate();
        flat.setRealEstateType(RealEstateType.FLAT);
        model.addAttribute("estate", flat);
        return "newFlat";
    }

    @GetMapping("/edit/{id}")
    public String getRealEstateEdit(@PathVariable Long id, Model model, @AuthenticationPrincipal Client client) {
        RealEstate realEstate = estateService.findById(id);

        if (realEstate == null) {
            return "redirect:/estates/";
        }

        if (!realEstate.getClient().getClientId().equals(client.getClientId()) && client.getRole() != Role.ADMIN) {
            return "redirect:/estates/";
        }

        String link = realEstate.getFlat() == null ? "/estates/edit/" : "/estates/editFlat/";
        model.addAttribute("link", link);
        model.addAttribute("estate", realEstate);
        return "editEstate";
    }

    @PutMapping("/edit/")
    public String update(@Validated(BasicEstateValidate.class) @ModelAttribute("estate") RealEstate realEstate,
                         BindingResult bindingResult, @AuthenticationPrincipal Client client) {

        if (bindingResult.hasErrors()) {
            return "editEstate";
        }

        realEstate.setClient(client);
        estateService.save(realEstate);
        return "redirect:/estates/";
    }

    @PutMapping("/editFlat/")
    public String updateFlat(@Validated({FlatValidate.class, BasicEstateValidate.class}) @ModelAttribute("estate") RealEstate realEstate,
                             BindingResult bindingResult, @AuthenticationPrincipal Client client) {

        if (bindingResult.hasErrors()) {
            return "editEstate";
        }

        realEstate.setClient(client);

        estateService.save(realEstate);
        return "redirect:/estates/";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        estateService.delete(id);
        return "redirect:/estates/";
    }

    @PostMapping("/newEstate")
    public String create(@Validated(BasicEstateValidate.class) @ModelAttribute("estate") RealEstate realEstate, BindingResult bindingResult,
                         @AuthenticationPrincipal Client client) {
        if (bindingResult.hasErrors()) {
            return "newEstate";
        }
        realEstate.setFlat(null);
        realEstate.setClient(client);
        estateService.save(realEstate);
        return "redirect:/estates/";
    }

    @PostMapping("/newFlat")
    public String createFlat(@Validated({FlatValidate.class, BasicEstateValidate.class}) @ModelAttribute("estate") RealEstate realEstate, BindingResult bindingResult,
                             @AuthenticationPrincipal Client client) {

        if (bindingResult.hasErrors()) {
            return "newFlat";
        }

        realEstate.setClient(client);
        estateService.save(realEstate);
        return "redirect:/estates/";
    }

    @GetMapping("/myEstates")
    public String myEstates(Model model, @AuthenticationPrincipal Client client) {
        List<RealEstate> estates = estateService.findClientEstates(client);
        model.addAttribute("estates", estates);
        return "clientCatalog";
    }

    private void setPaginatedCatalog(Model model, int page, String sort, String type, List<RealEstate> catalog, long lastPage) {
        model.addAttribute("estates", catalog);
        model.addAttribute("page", page);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("sort", sort);
        model.addAttribute("type", type);
    }

}
