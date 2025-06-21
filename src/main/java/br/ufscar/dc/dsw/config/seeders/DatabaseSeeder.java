package br.ufscar.dc.dsw.config.seeders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder {

    @Autowired
    private UsuarioSeeder usuarioSeeder;

    @Autowired
    private EstrategiaSeeder estrategiaSeeder;

    public void seedDatabase() {
        System.out.println("Iniciando seed do banco de dados...");
        
        usuarioSeeder.seedUsuarios();
        estrategiaSeeder.seedEstrategias();
        
        System.out.println("Seed do banco de dados concluído!");
    }
} 