package br.ufscar.dc.dsw;

import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.models.enums.Papel;
import br.ufscar.dc.dsw.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EspressoGamingTestingJpaApplication {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder; // BCryptPasswordEncoder ainda é injetado, mas não usado diretamente aqui para salvar

	public static void main(String[] args) {
		SpringApplication.run(EspressoGamingTestingJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializationRunner() {
		return args -> {
			// Criar usuário 'admin' se não existir
			UsuarioModel adminExistente = usuarioService.buscarPorLogin("admin@admin.com");
			if (adminExistente == null) { // <--- CORREÇÃO AQUI: Verifique se o objeto é null
				UsuarioModel admin = new UsuarioModel();
				admin.setNome("Administrador");
				admin.setEmail("admin@admin.com");
				admin.setSenha("admin"); // A senha será codificada pelo serviço
				admin.setPapel(Papel.ADMIN);
				usuarioService.salvar(admin);
				System.out.println("Usuário 'admin' criado.");
			} else {
				System.out.println("Usuário 'admin' já existe.");
			}

			// Criar usuário 'tester' se não existir
			UsuarioModel testerExistente = usuarioService.buscarPorLogin("tester@tester.com");
			if (testerExistente == null) { // <--- CORREÇÃO AQUI: Verifique se o objeto é null
				UsuarioModel tester = new UsuarioModel();
				tester.setNome("Tester");
				tester.setEmail("tester@tester.com");
				tester.setSenha("tester"); // A senha será codificada pelo serviço
				tester.setPapel(Papel.TESTER);
				usuarioService.salvar(tester);
				System.out.println("Usuário 'tester' criado.");
			} else {
				System.out.println("Usuário 'tester' já existe.");
			}
		};
	}
}