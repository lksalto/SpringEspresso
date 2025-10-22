package br.ufscar.dc.dsw.projeto.controller;

import br.ufscar.dc.dsw.projeto.dto.ProjetoCadastroDTO;
import br.ufscar.dc.dsw.projeto.dto.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.service.EstrategiaService;
import br.ufscar.dc.dsw.projeto.service.ProjetoService;
import br.ufscar.dc.dsw.projeto.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private EstrategiaService estrategiaService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public String listarProjetos(Model model) {
        List<ProjetoModel> projetos = projetoService.listarTodos();
        model.addAttribute("projetos", projetos);
        return "projeto/lista";
    }

    @GetMapping("/detalhes/{id}")
    public String exibirDetalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        ProjetoModel projeto = projetoService.buscar(id);
        if (projeto == null) {
            redirectAttributes.addFlashAttribute("fail", "Projeto não encontrado.");
            return "redirect:/projetos/listar";
        }
        model.addAttribute("projeto", projeto);
        return "projeto/detalhes";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        ProjetoCadastroDTO dto = new ProjetoCadastroDTO();
        model.addAttribute("projetoCadastroDTO", dto);
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "projeto/formulario";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cadastro")
    public String formCadastro(Model model) {
        return exibirFormularioCadastro(model);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/salvar")
    public String salvarProjeto(@ModelAttribute ProjetoCadastroDTO dto, RedirectAttributes redirectAttributes) {
        try {
            projetoService.salvar(dto);
            redirectAttributes.addFlashAttribute("success", "Projeto cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao cadastrar projeto: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        ProjetoModel projeto = projetoService.buscar(id);
        if (projeto == null) {
            redirectAttributes.addFlashAttribute("fail", "Projeto não encontrado.");
            return "redirect:/projetos/listar";
        }

        ProjetoEdicaoDTO dto = new ProjetoEdicaoDTO();
        dto.setId(projeto.getId());
        dto.setNome(projeto.getNome());
        dto.setDescricao(projeto.getDescricao());
        
        if (projeto.getEstrategias() != null) {
            dto.setEstrategiasIds(projeto.getEstrategias().stream()
                    .map(EstrategiaModel::getId)
                    .collect(Collectors.toList()));
        }
        
        if (projeto.getMembros() != null) {
            dto.setMembrosIds(projeto.getMembros().stream()
                    .map(UsuarioModel::getId)
                    .collect(Collectors.toList()));
        }

        model.addAttribute("projetoEdicaoDTO", dto);
        model.addAttribute("estrategias", estrategiaService.buscarTodas());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "projeto/formulario";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar")
    public String editarProjeto(@ModelAttribute ProjetoEdicaoDTO dto, RedirectAttributes redirectAttributes) {
        try {
            projetoService.atualizar(dto);
            redirectAttributes.addFlashAttribute("success", "Projeto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao atualizar projeto: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/remover/{id}")
    public String removerProjeto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            projetoService.remover(id);
            redirectAttributes.addFlashAttribute("success", "Projeto removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("fail", "Erro ao remover projeto: " + e.getMessage());
        }
        return "redirect:/projetos/listar";
    }
}
