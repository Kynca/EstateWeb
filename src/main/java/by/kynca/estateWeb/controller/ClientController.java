package by.kynca.estateWeb.controller;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class ClientController {

    private final ClientService service;

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
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
}
