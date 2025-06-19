package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.models.UsuarioModel;
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
@RequestMapping("/usuarios") // Mapeamento base para todas as requisições deste controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos os usuários
    @GetMapping("/listar")
    public String listar(ModelMap model) {
        List<UsuarioModel> usuarios = usuarioService.buscarTodos();
        model.addAttribute("listaUsuarios", usuarios);
        model.addAttribute("contextPath", "/usuarios"); // Necessário para links relativos na view
        return "usuario/lista"; // Nome da view Thymeleaf (ex: src/main/resources/templates/usuario/lista.html)
    }

    // Apresentar formulário de cadastro (GET)
    @GetMapping("/cadastro")
    public String preRenderCadastro(UsuarioModel usuario) { // Usuario é injetado vazio para o formulário
        return "usuario/formulario";
    }

    // Inserir novo usuário (POST)
    @PostMapping("/salvar")
    public String salvar(@Valid UsuarioModel usuario, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "usuario/formulario"; // Volta para o formulário com os erros de validação
        }
        try {
            usuario.setPapel(Papel.valueOf("tester")); // Define o papel padrão como 'tester' para novos cadastros via formulário
            // Você pode querer um formulário separado para admins ou admins definindo o papel
            usuarioService.salvar(usuario);
            attr.addFlashAttribute("success", "Usuário salvo com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage()); // Mensagem de erro de negócio (ex: login duplicado)
            return "redirect:/usuarios/cadastro"; // Redireciona para evitar reenvio do formulário
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao salvar usuário: " + e.getMessage());
            return "redirect:/usuarios/cadastro";
        }
        return "redirect:/usuarios/listar";
    }

    // Apresentar formulário de edição (GET)
    @GetMapping("/editar/{id}")
    public String preRenderEdicao(@PathVariable("id") UUID id, ModelMap model, RedirectAttributes attr) {
        try {
            UsuarioModel usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuario", usuario);
            return "usuario/formulario";
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            return "redirect:/usuarios/listar";
        }
    }

    // Atualizar usuário (POST)
    @PostMapping("/editar") // Para edição, o ID vem no objeto Usuario
    public String editar(@Valid UsuarioModel usuario, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            return "usuario/formulario";
        }
        try {
            // Ao editar, garantir que o papel não seja alterado sem intenção, ou permitir que seja editado se for o caso
            // Se o papel não vier do formulário, buscar o papel original:
            // Usuario original = usuarioService.buscarPorId(usuario.getId());
            // usuario.setPapel(original.getPapel());

            usuarioService.salvar(usuario); // O método salvar do service já trata se é insert ou update
            attr.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            attr.addFlashAttribute("fail", e.getMessage());
            return "redirect:/usuarios/editar/" + usuario.getId();
        } catch (RuntimeException e) {
            attr.addFlashAttribute("fail", "Erro ao atualizar usuário: " + e.getMessage());
            return "redirect:/usuarios/editar/" + usuario.getId();
        }
        return "redirect:/usuarios/listar";
    }


    // Remover usuário (GET ou POST) - Recomenda-se POST para remoções
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