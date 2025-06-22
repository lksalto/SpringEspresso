package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.UsuarioCadastroDTO;
import br.ufscar.dc.dsw.dtos.UsuarioDTO;
import br.ufscar.dc.dsw.dtos.UsuarioEdicaoDTO;
import br.ufscar.dc.dsw.models.enums.Papel;
import br.ufscar.dc.dsw.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos os usuários
    @GetMapping("/listar")
    public String listar(ModelMap model) {
        List<UsuarioDTO> usuarios = usuarioService.buscarTodos();
        model.addAttribute("listaUsuarios", usuarios);
        model.addAttribute("contextPath", "/usuarios");
        return "usuario/lista";
    }

    // Apresentar formulário de cadastro (GET)
    @GetMapping("/cadastro")
    public String preRenderCadastro(ModelMap model) {
        if (!model.containsAttribute("usuarioForm")) {
            model.addAttribute("usuarioForm", new UsuarioCadastroDTO(null, null, null, null, null)); // UUID id, String nome, String email, String senha, Papel papel
        }
        return "usuario/formulario";
    }

    // Inserir novo usuário (POST)
    @PostMapping("/salvar")
    public String salvar(@Valid UsuarioCadastroDTO usuarioCadastroDTO, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("usuarioForm", usuarioCadastroDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioForm", result);
            return "redirect:/usuarios/cadastro";
        }
        try {
            // No need to set Papel here, it's already in the DTO from the form
            usuarioService.salvarNovoUsuario(usuarioCadastroDTO);
            attr.addFlashAttribute("success", "Usuário salvo com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            attr.addFlashAttribute("usuarioForm", usuarioCadastroDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioForm", result);
            return "redirect:/usuarios/cadastro";
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao salvar usuário: " + e.getMessage());
            attr.addFlashAttribute("usuarioForm", usuarioCadastroDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioForm", result);
            return "redirect:/usuarios/cadastro";
        }
        return "redirect:/usuarios/listar";
    }

    // Apresentar formulário de edição (GET)
    @GetMapping("/editar/{id}")
    public String preRenderEdicao(@PathVariable("id") UUID id, ModelMap model, RedirectAttributes attr) {
        try {
            if (!model.containsAttribute("usuarioForm")) {
                UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);
                UsuarioEdicaoDTO usuarioEdicaoDTO = new UsuarioEdicaoDTO(
                        usuarioDTO.id(),
                        usuarioDTO.nome(),
                        usuarioDTO.email(),
                        null, // Password is not returned in UsuarioDTO, so set to null for initial edit form load
                        usuarioDTO.papel()
                );
                model.addAttribute("usuarioForm", usuarioEdicaoDTO);
            }
            return "usuario/formulario";
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }

    // Atualizar usuário (POST)
    @PostMapping("/editar")
    public String editar(@Valid UsuarioEdicaoDTO usuarioEdicaoDTO, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("usuarioForm", usuarioEdicaoDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioForm", result);
            return "redirect:/usuarios/editar/" + usuarioEdicaoDTO.id();
        }
        try {
            usuarioService.atualizarUsuario(usuarioEdicaoDTO);
            attr.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            attr.addFlashAttribute("usuarioForm", usuarioEdicaoDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioForm", result);
            return "redirect:/usuarios/editar/" + usuarioEdicaoDTO.id();
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao atualizar usuário: " + e.getMessage());
            attr.addFlashAttribute("usuarioForm", usuarioEdicaoDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioForm", result);
            return "redirect:/usuarios/editar/" + usuarioEdicaoDTO.id();
        }
        return "redirect:/usuarios/listar";
    }

    // Remover usuário (GET ou POST)
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable("id") UUID id, RedirectAttributes attr) {
        try {
            usuarioService.excluir(id);
            attr.addFlashAttribute("success", "Usuário removido com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao remover usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }
}