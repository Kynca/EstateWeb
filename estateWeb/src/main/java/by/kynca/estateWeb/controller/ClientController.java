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

    @GetMapping("login")
    public String login(){
        return "loginForm";
    }

    @Autowired
    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping("register")
    public String registerGet(Model model){
        model.addAttribute("client", new Client());
        return "newClient";
    }

    @PostMapping("register")
    public String register(@Valid @ModelAttribute Client client, BindingResult bindingResult, Model model){
//            return "newClient";
        if(service.signUp(client) == null){
            model.addAttribute("mailError", "not unique mail");
            return "newClient";
        }
        return "redirect:/login";
    }
}
