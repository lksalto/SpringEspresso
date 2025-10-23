package br.ufscar.dc.dsw.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjetoApplication {

    public static void main(String[] args) {
        System.out.println(">>> INICIALIZANDO APLICAÇÃO COM SCHEDULING HABILITADO <<<");
        SpringApplication.run(ProjetoApplication.class, args);
    }
}
