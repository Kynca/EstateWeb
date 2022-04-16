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

/**
 * Controller for actions with real estates
 */
@Controller
@RequestMapping("/estates")
public class RealEstateController {

    private final RealEstateService estateService;

    @Autowired
    public RealEstateController(RealEstateService estateService) {
        this.estateService = estateService;
    }

    /**
     * request for list of real estates
     * @param page number of page(pagination
     * @param sort type of sort
     * @param model used for adding list of real estates
     * @return page with real estates
     */
    @GetMapping("/")
    public String catalog(@RequestParam("page") Optional<Integer> page, @RequestParam("sort") Optional<String> sort, Model model) {
        int currentPage = page.orElse(0);
        int lastPage = estateService.getAllPagesAmount();

        if (currentPage > lastPage ){
            currentPage = 0;
        }

        List<RealEstate> catalog = estateService.findAll(currentPage, sort.orElse("realEstateId"));
        setPaginatedCatalog(model, currentPage, sort.orElse("realEstateId"), "", catalog, lastPage);
        return "catalog";
    }

    /**
     * request for list of real estates of certain type
     * @param page number of page
     * @param sort type os sort
     * @param estateType type of real estate(in my case house, land or flat)
     * @param model used for adding list of real estates
     * @return catalog page with real estates
     */
    @GetMapping("/{estateType}")
    public String specificCatalog(@RequestParam("page") Optional<Integer> page, @RequestParam("sort") Optional<String> sort,
                                  @PathVariable String estateType, Model model) {

        int currentPage = page.orElse(0);
        int lastPage = estateService.getAllPagesAmount();

        if (currentPage > lastPage ){
            currentPage = 0;
        }

        List<RealEstate> catalog = estateService.findAllByType(estateType, currentPage, sort.orElse("realEstateId"));

        RealEstateType type = catalog.get(0).getRealEstateType();
        setPaginatedCatalog(model, page.orElse(0), sort.orElse("realEstateId"),
                type.getValue().toLowerCase() + "/", catalog, estateService.getByTypePagesAmount(type));
        return "catalog";
    }

    /**
     * request for real estate with id
     * @param id of searching real estate
     * @param model used to add real estate
     * @return page with estate
     */
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

    /**
     * preparation request for adding new real estate( without flat)
     * @param model used to add new real estate class
     * @return page with form for creation new real estate
     */
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("estate", new RealEstate());
        return "newEstate";
    }

    /**
     * preparation request for adding new real estate(with flat)
     * @param model used to add new real estate class
     * @return page with form for creation new real estate
     */
    @GetMapping("/newFlat")
    public String flatForm(Model model) {
        RealEstate flat = new RealEstate();
        flat.setRealEstateType(RealEstateType.FLAT);
        model.addAttribute("estate", flat);
        return "newFlat";
    }

    /**
     * preparation request editing user
     * @param id of estate which will be edited
     * @param model used for add real estate to page
     * @param client authorized client
     * @return if client id same with real estate client id or user is admin and real estate wih id exist return page with edit form
     * else redirect to page with catalog
     */
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

    /**
     * request for editing real estate(without flat)
     * @param realEstate edited real estate
     * @param bindingResult which contains errors
     * @param client authorized client
     * @return page form if realEstate is not valid if it valid return page with calaog
     */
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

    /**
     * request for editing real estate(with flat)
     * @param realEstate edited real estate
     * @param bindingResult which contains errors
     * @param client authorized client
     * @return page form if realEstate is not valid if it valid return page with calaog
     */
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

    /**
     * request for deleting client
     * @param id of real estate for delete
     * @return page with catalog
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        estateService.delete(id);
        return "redirect:/estates/";
    }

    /**
     * request for adding new estate(without flat)
     * @param realEstate which will be added to the db
     * @param bindingResult contains errors
     * @param client authorized client
     * @return if real estate is valid return catalog if not return same form
     */
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

    /**
     * request for adding new estate(with flat)
     * @param realEstate which will be added to the db
     * @param bindingResult contains errors
     * @param client authorized client
     * @return if real estate is valid return catalog if not return same form
     */
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

    /**
     * request for getting list of estates certain client
     * @param model used to add list of estates to page
     * @param client authorized client
     * @return page with list with estates of certain client
     */
    @GetMapping("/myEstates")
    public String myEstates(Model model, @AuthenticationPrincipal Client client) {
        List<RealEstate> estates = estateService.findClientEstates(client);
        model.addAttribute("estates", estates);
        return "clientCatalog";
    }

    /**
     * additional function for setting information for pagination
     * @param page curent page of pagination
     * @param sort type of sort
     * @param type type of estate
     * @param catalog list of estates
     * @param lastPage last page of pagination
     */
    private void setPaginatedCatalog(Model model, long page, String sort, String type, List<RealEstate> catalog, long lastPage) {
        model.addAttribute("estates", catalog);
        model.addAttribute("page", page);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("sort", sort);
        model.addAttribute("type", type);
    }

}
