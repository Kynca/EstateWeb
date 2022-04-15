package by.kynca.estateWeb.controller;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("")
    public String homepage(){
        return "redirect:/estates/";
    }

    @GetMapping("login")
    public String login() {
        return "loginForm";
    }


    @GetMapping("register")
    public String registerGet(Model model) {
        model.addAttribute("client", new Client());
        return "newClient";
    }

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

    @GetMapping("clients")
    public String viewUsers(@RequestParam("page") Optional<Integer> page, Model model){
       List<Client> clients = service.findAll(page.orElse(0), "clientId");
       model.addAttribute("clients", clients);
       return "viewClients";
    }

    @PutMapping("client/{id}")
    public String setEnable(@PathVariable Long id){
        service.setEnable(id);
        return "redirect:/clients";
    }
}
