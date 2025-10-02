package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.service.ProjetoService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("listaProjetos", projetoService.listar());
        return "projeto/lista";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("projeto", new ProjetoModel());
        return "projeto/cadastro";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ProjetoModel projeto) {
        projetoService.salvar(projeto);
        return "redirect:/projetos/listar";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable UUID id) {
        projetoService.remover(id);
        return "redirect:/projetos/listar";
    }
}
