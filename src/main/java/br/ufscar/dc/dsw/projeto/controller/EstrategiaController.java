package br.ufscar.dc.dsw.projeto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.service.EstrategiaService;

import java.util.List;

@Controller
public class EstrategiaController {

    private final EstrategiaService estrategiaService;

    public EstrategiaController(EstrategiaService estrategiaService) {
        this.estrategiaService = estrategiaService;
    }

    @GetMapping("/estrategias")
    public String listar(Model model) {
        List<EstrategiaModel> estrategias = estrategiaService.buscarTodas(); // ou buscarTodos()
        model.addAttribute("estrategias", estrategias);
        return "estrategias/listar";
    }

    @GetMapping("/estrategias/novo")
    public String novo() {
        return "estrategias/novo"; // Página para criar nova estratégia
    }

    @GetMapping("/estrategias/editar/{id}")
    public String editar(@PathVariable("id") Long id, Model model) {
        EstrategiaModel estrategia = estrategiaService.buscarPorId(id);
        model.addAttribute("estrategia", estrategia);
        return "estrategias/editar";
    }

    @GetMapping("/estrategias/remover/{id}")
    public String remover(@PathVariable("id") Long id, Model model) {
        estrategiaService.remover(id);
        return "redirect:/estrategias";
    }

    @GetMapping("/estrategias/detalhes/{id}")
    public String detalhes(@PathVariable("id") Long id, Model model) {
        EstrategiaModel estrategia = estrategiaService.buscarPorId(id);
        model.addAttribute("estrategia", estrategia);
        return "estrategias/detalhes";
    }
}
