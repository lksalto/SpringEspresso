package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.dto.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.service.EstrategiaService;
import br.ufscar.dc.dsw.projeto.service.ProjetoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final EstrategiaService estrategiaService;

    public ProjetoController(ProjetoService projetoService, EstrategiaService estrategiaService) {
        this.projetoService = projetoService;
        this.estrategiaService = estrategiaService;
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

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") String id, Model model) {
        ProjetoModel projeto = projetoService.buscar(java.util.UUID.fromString(id));
        if (projeto == null) {
            return "redirect:/projetos/listar";
        }

        ProjetoEdicaoDTO dto = new ProjetoEdicaoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        dto.setEstrategiasIds(projeto.getEstrategias().stream().map(EstrategiaModel::getId).collect(Collectors.toList()));
        // Adicionar membrosIds se houver

        model.addAttribute("projetoEdicaoDTO", dto);
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        // Adicionar lista de usuários ao modelo se houver
        return "projeto/formulario";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute("projetoEdicaoDTO") ProjetoEdicaoDTO dto) {
        projetoService.atualizar(dto);
        return "redirect:/projetos/listar";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable("id") String id) {
        projetoService.remover(java.util.UUID.fromString(id));
        return "redirect:/projetos/listar";
    }
}
