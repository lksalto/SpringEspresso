package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.dto.ProjetoCadastroDTO;
import br.ufscar.dc.dsw.projeto.dto.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.service.EstrategiaService;
import br.ufscar.dc.dsw.projeto.service.ProjetoService;
import br.ufscar.dc.dsw.projeto.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;
    private final EstrategiaService estrategiaService;
    private final UsuarioService usuarioService;

    public ProjetoController(ProjetoService projetoService, EstrategiaService estrategiaService, UsuarioService usuarioService) {
        this.projetoService = projetoService;
        this.estrategiaService = estrategiaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("listaProjetos", projetoService.listar());
        return "projeto/lista";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        ProjetoCadastroDTO dto = new ProjetoCadastroDTO();
        List<EstrategiaModel> todasEstrategias = estrategiaService.buscarTodas();
        List<Long> todasEstrategiasIds = todasEstrategias.stream()
                .map(EstrategiaModel::getId)
                .collect(Collectors.toList());
        dto.setEstrategiasIds(todasEstrategiasIds);

        model.addAttribute("projetoCadastroDTO", dto);
        model.addAttribute("estrategias", todasEstrategias);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "projeto/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("projetoCadastroDTO") ProjetoCadastroDTO dto) {
        projetoService.salvar(dto);
        return "redirect:/projetos/listar";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable("id") Long id, Model model) {
        ProjetoModel projeto = projetoService.buscar(id);
        if (projeto == null) {
            return "redirect:/projetos/listar";
        }
        model.addAttribute("projeto", projeto);
        return "projeto/detalhes";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Long id, Model model) {
        ProjetoModel projeto = projetoService.buscar(id);
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
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "projeto/formulario";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute("projetoEdicaoDTO") ProjetoEdicaoDTO dto) {
        projetoService.atualizar(dto);
        return "redirect:/projetos/listar";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable("id") Long id) {
        projetoService.remover(id);
        return "redirect:/projetos/listar";
    }
}
