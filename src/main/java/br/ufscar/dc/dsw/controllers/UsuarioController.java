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

    @GetMapping("/listar")
    public String listar(ModelMap model) {
        List<UsuarioDTO> usuarios = usuarioService.buscarTodos();
        model.addAttribute("listaUsuarios", usuarios);
        model.addAttribute("contextPath", "/usuarios");
        return "usuario/lista";
    }

    @GetMapping("/cadastro")
    public String preRenderCadastro(UsuarioCadastroDTO usuarioCadastroDTO) {
        return "usuario/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid UsuarioCadastroDTO usuarioCadastroDTO, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("usuarioCadastroDTO", usuarioCadastroDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioCadastroDTO", result);
            return "redirect:/usuarios/cadastro";
        }
        try {
            usuarioService.salvarNovoUsuario(usuarioCadastroDTO);
            attr.addFlashAttribute("success", "Usuário salvo com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            attr.addFlashAttribute("usuarioCadastroDTO", usuarioCadastroDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioCadastroDTO", result);
            return "redirect:/usuarios/cadastro";
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao salvar usuário: " + e.getMessage());
            attr.addFlashAttribute("usuarioCadastroDTO", usuarioCadastroDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioCadastroDTO", result);
            return "redirect:/usuarios/cadastro";
        }
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String preRenderEdicao(@PathVariable("id") UUID id, ModelMap model, RedirectAttributes attr) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);
            // Convert UsuarioDTO to UsuarioEdicaoDTO for the form
            UsuarioEdicaoDTO usuarioEdicaoDTO = new UsuarioEdicaoDTO(
                    usuarioDTO.id(),
                    usuarioDTO.nome(),
                    usuarioDTO.email(),
                    null, // Password is not returned in UsuarioDTO, so set to null for initial edit form load
                    usuarioDTO.papel()
            );

            model.addAttribute("usuarioEdicaoDTO", usuarioEdicaoDTO);
            return "usuario/formulario";
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }

    @PostMapping("/editar")
    public String editar(@Valid UsuarioEdicaoDTO usuarioEdicaoDTO, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("usuarioEdicaoDTO", usuarioEdicaoDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioEdicaoDTO", result);
            return "redirect:/usuarios/editar/" + usuarioEdicaoDTO.id();
        }
        try {
            usuarioService.atualizarUsuario(usuarioEdicaoDTO);
            attr.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            attr.addFlashAttribute("usuarioEdicaoDTO", usuarioEdicaoDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioEdicaoDTO", result);
            return "redirect:/usuarios/editar/" + usuarioEdicaoDTO.id();
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao atualizar usuário: " + e.getMessage());
            attr.addFlashAttribute("usuarioEdicaoDTO", usuarioEdicaoDTO);
            attr.addFlashAttribute("org.springframework.validation.BindingResult.usuarioEdicaoDTO", result);
            return "redirect:/usuarios/editar/" + usuarioEdicaoDTO.id();
        }
        return "redirect:/usuarios/listar";
    }

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