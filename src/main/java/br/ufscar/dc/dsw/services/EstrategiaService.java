package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.DicaDto;
import br.ufscar.dc.dsw.dtos.EstrategiaDto;
import br.ufscar.dc.dsw.dtos.ExemploDto;
import br.ufscar.dc.dsw.models.DicaModel;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.ExemploModel;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;

@Service
@Transactional
public class EstrategiaService {

    @Autowired
    private EstrategiaRepository estrategiaRepository;

    private final Path uploadLocation = Paths.get("src/main/resources/static/uploads/estrategias");

    public EstrategiaService() {
        try {
            Files.createDirectories(uploadLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload.", e);
        }
    }

@PreAuthorize("hasRole('ADMIN')")
public EstrategiaModel save(EstrategiaDto dto, List<MultipartFile> imagensExemplo) {
    EstrategiaModel estrategia = convertDtoToModel(dto);

    // garante lista inicializada
    if (estrategia.getExemplos() == null) {
        estrategia.setExemplos(new ArrayList<>());
    }

    // baseName sanitizado (usa o nome da estratégia)
    String baseName = sanitize(estrategia.getNome());

    if (imagensExemplo != null && !imagensExemplo.isEmpty()) {
        int i = 0;
        for (ExemploModel exemplo : estrategia.getExemplos()) {
            if (exemplo == null) continue;
            if (i < imagensExemplo.size()) {
                MultipartFile imagem = imagensExemplo.get(i);
                if (imagem != null && !imagem.isEmpty()) {
                    // exclui imagem antiga se existir
                    if (exemplo.getUrlImagem() != null && !exemplo.getUrlImagem().isEmpty()) {
                        deleteFile(exemplo.getUrlImagem());
                    }
                    // salva com sequência baseada no nome da estratégia
                    String newFilename = saveFileWithSequence(imagem, baseName);
                    exemplo.setUrlImagem(newFilename);
                }
            }
            i++;
        }
    }

        return estrategiaRepository.save(estrategia);
    }



    @Transactional(readOnly = true)
    public EstrategiaModel findById(UUID id) {
        return estrategiaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<EstrategiaModel> findAll() {
        return estrategiaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        EstrategiaModel estrategia = findById(id);
        if (estrategia != null) {
            estrategia.getExemplos().forEach(ex -> {
                if (ex.getUrlImagem() != null && !ex.getUrlImagem().isEmpty()) {
                    deleteFile(ex.getUrlImagem());
                }
            });
            estrategiaRepository.deleteById(id);
        }
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) return null;
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new RuntimeException("Arquivo sem extensão não é permitido.");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        try {
            Path destinationFile = this.uploadLocation.resolve(newFilename).normalize().toAbsolutePath();
            System.out.println(destinationFile + "    " + newFilename);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo.", e);
        }
    }

    private void deleteFile(String filename) {
        try {
            Path file = uploadLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("ERRO ao excluir arquivo: " + filename);
        }
    }

private EstrategiaModel convertDtoToModel(EstrategiaDto dto) {
    EstrategiaModel model;

    if (dto.getId() != null) {
        model = findById(dto.getId());
        if (model == null) {
            model = new EstrategiaModel();
            model.setId(null);
        }
    } else {
        model = new EstrategiaModel();
    }

    model.setNome(dto.getNome());
    model.setDescricao(dto.getDescricao());

    // Dicas
    model.getDicas().clear();
    if (dto.getDicas() != null) {
        for (DicaDto dicaDto : dto.getDicas()) {
            DicaModel dicaModel = new DicaModel();
            if (dicaDto.getId() != null) {
                dicaModel.setId(dicaDto.getId());
            }
            dicaModel.setTexto(dicaDto.getDica());
            dicaModel.setEstrategia(model);
            model.getDicas().add(dicaModel);
        }
    }

    // Exemplos
    List<ExemploModel> existentes = new ArrayList<>();
    if (model.getExemplos() != null) {
        existentes.addAll(model.getExemplos());
    }
    model.getExemplos().clear();

    int ordem = existentes.stream()
                          .mapToInt(ExemploModel::getOrdem)
                          .max()
                          .orElse(0) + 1;

    if (dto.getExemplos() != null) {
        for (ExemploDto exemploDto : dto.getExemplos()) {
            ExemploModel exemploModel = null;

            // Se já existe, reutiliza
            if (exemploDto.getId() != null) {
                final UUID id = exemploDto.getId();
                exemploModel = existentes.stream()
                        .filter(e -> e.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            // Se não existe, cria novo
            if (exemploModel == null) {
                exemploModel = new ExemploModel();
                exemploModel.setOrdem(ordem++);
            }

            exemploModel.setTexto(exemploDto.getTexto());

            if (exemploDto.getUrlImagem() != null && !exemploDto.getUrlImagem().isEmpty()) {
                exemploModel.setUrlImagem(exemploDto.getUrlImagem());
            }

            exemploModel.setEstrategia(model);
            model.getExemplos().add(exemploModel);
        }
    }

    // Opcional: ordenar por ordem antes de salvar
    model.getExemplos().sort((a, b) -> Integer.compare(a.getOrdem(), b.getOrdem()));

    return model;
}


    public EstrategiaDto convertModelToDto(EstrategiaModel model) {
    EstrategiaDto dto = new EstrategiaDto();
    dto.setId(model.getId());
    dto.setNome(model.getNome());
    dto.setDescricao(model.getDescricao());

    // Dicas
    List<DicaDto> dicasDto = new ArrayList<>();
    if (model.getDicas() != null) {
        model.getDicas().forEach(dicaModel -> {
            DicaDto dicaDto = new DicaDto();
            dicaDto.setId(dicaModel.getId());
            dicaDto.setDica(dicaModel.getTexto());
            dicasDto.add(dicaDto);
        });
    }
    dto.setDicas(dicasDto);

    // Exemplos - ordenados por ID (ou data de criação, se houver)
    List<ExemploDto> exemplosDto = new ArrayList<>();
    if (model.getExemplos() != null) {
        model.getExemplos().stream()
             .sorted((e1, e2) -> e1.getId().compareTo(e2.getId())) // ordena por ID
             .forEach(exemploModel -> {
                 ExemploDto exemploDto = new ExemploDto();
                 exemploDto.setId(exemploModel.getId());
                 exemploDto.setTexto(exemploModel.getTexto());
                 exemploDto.setUrlImagem(exemploModel.getUrlImagem());
                 exemplosDto.add(exemploDto);
             });
    }
    dto.setExemplos(exemplosDto);

    return dto;
}

private String sanitize(String input) {
    if (input == null) return "estrategia";
    // remove acentos, normaliza, remove chars não permitidos e troca espaços por '-'
    String s = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .toLowerCase();
    s = s.replaceAll("[^a-z0-9\\-\\_ ]", ""); // mantém letras, números, - _ e espaços
    s = s.trim().replaceAll("\\s+", "-");     // espaços -> '-'
    if (s.isEmpty()) s = "estrategia";
    return s;
}

private int getNextSequenceForBaseName(String baseName) {
    try (Stream<Path> files = Files.list(this.uploadLocation)) {
        // padrão: baseName_<num>.<ext>
        Pattern p = Pattern.compile(Pattern.quote(baseName) + "_(\\d+)\\.[^\\.]+$");
        int max = 0;
        for (Path f : (Iterable<Path>) files::iterator) {
            String name = f.getFileName().toString();
            Matcher m = p.matcher(name);
            if (m.find()) {
                try {
                    int num = Integer.parseInt(m.group(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return max + 1;
    } catch (IOException e) {
        // em caso de erro, começamos do 1
        return 1;
    }
}

private String saveFileWithSequence(MultipartFile file, String baseName) {
    if (file == null || file.isEmpty()) return null;

    String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
    if (originalFilename == null || !originalFilename.contains(".")) {
        throw new RuntimeException("Arquivo sem extensão não é permitido.");
    }
    String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    // obter sequência
    int seq = getNextSequenceForBaseName(baseName);
    String finalFilename = baseName + "_" + seq + extension;

    try {
        Path destinationFile = this.uploadLocation.resolve(finalFilename).normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        return finalFilename; // retorna somente o nome, sem path
    } catch (IOException e) {
        throw new RuntimeException("Falha ao salvar o arquivo.", e);
    }
}



}