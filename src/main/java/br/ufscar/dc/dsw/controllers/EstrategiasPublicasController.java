package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.services.EstrategiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/estrategias")
public class EstrategiasPublicasController {

    @Autowired
    private EstrategiaService estrategiaService;

    @GetMapping("/publicas")
    public String listarEstrategiasPublicas(Model model) {
        List<EstrategiaModel> estrategias = estrategiaService.buscarEstrategiasPublicas();
        model.addAttribute("estrategias", estrategias);
        return "estrategias/lista_publicas";
    }

    // Outros métodos e mapeamentos do controller
}