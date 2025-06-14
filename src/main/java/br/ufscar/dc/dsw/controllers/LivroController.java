package br.ufscar.dc.dsw.controllers;

import br.ufscar.dc.dsw.dtos.LivroRecordDto;
import br.ufscar.dc.dsw.models.LivroModel;
import br.ufscar.dc.dsw.services.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/livrariajpa/livros")
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<List<LivroModel>> getAllLivros() {
        return ResponseEntity.status(HttpStatus.OK).body(livroService.getAllLivros());
    }

    @PostMapping
    public ResponseEntity<LivroModel> saveLivro(@RequestBody LivroRecordDto livroRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.saveLivro(livroRecordDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivro(@PathVariable UUID id) {
        livroService.deleteLivro(id);
        return ResponseEntity.status(HttpStatus.OK).body("Livro com id " + id + " foi deletado com sucesso.");
    }
}
