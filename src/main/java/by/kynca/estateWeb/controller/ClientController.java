package by.kynca.estateWeb.controller;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controller for actions with clients
 */
@Controller
@RequestMapping("/")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    /**
     * redirect  to home page
     * @return page with real estates
     */
    @GetMapping("")
    public String homepage(){
        return "redirect:/estates/";
    }

    /**
     * @return login page if client not authorized
     */
    @GetMapping("login")
    public String loginForm(@AuthenticationPrincipal Client client) {
       return client == null ? "loginForm" : "redirect:/estates/";
    }


    /**
     * @param model used for adding client class
     * @return page with page for creating new client
     */
    @GetMapping("register")
    public String registerGet(Model model) {
        model.addAttribute("client", new Client());
        return "newClient";
    }

    /**
     * registration request
     * @param client client from form newClient page
     * @param bindingResult contains errors from client validation
     * @param model used for adding error with not uniq mail
     * @return if add client is successful redirect to login page if not return to the same page with errors
     */
    @PostMapping("register")
    public String register(@Valid @ModelAttribute Client client, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "newClient";
        }


        if (service.save(client) == null) {
            model.addAttribute("mailError", "not unique mail");
            return "newClient";
        }
        return "redirect:/login";
    }

    /**
     * getting clients request
     * @param page number of page
     * @param model used for adding list of client
     * @return page with table of clients
     */
    @GetMapping("clients")
    public String viewUsers(@RequestParam("page") Optional<Integer> page, Model model){
       List<Client> clients = service.findAll(page.orElse(0), "clientId");
       model.addAttribute("clients", clients);
       return "viewClients";
    }

    /**
     * request for setting client disabled
     * @param id of client
     * @return page with clients
     */
    @PutMapping("client/{id}")
    public String setEnable(@PathVariable Long id){
        service.setEnable(id);
        return "redirect:/clients";
    }
}
