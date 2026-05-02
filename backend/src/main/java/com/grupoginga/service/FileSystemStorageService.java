package com.grupoginga.service.storage;

import com.grupoginga.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path diretorioRaiz;

    // Injeta o valor que configuramos no application.properties
    public FileSystemStorageService(@Value("${grupoginga.storage.local.diretorio}") String diretorio) {
        this.diretorioRaiz = Paths.get(diretorio);

        // Cria a pasta automaticamente quando o sistema iniciar, se ela não existir
        try {
            Files.createDirectories(diretorioRaiz);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta de uploads", e);
        }
    }

    @Override
    public String salvar(MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException("Falha ao armazenar arquivo vazio.");
        }

        try {
            // Gera um nome único para evitar que fotos com o mesmo nome se sobrescrevam (ex: foto.jpg)
            String nomeOriginal = StringUtils.cleanPath(arquivo.getOriginalFilename());
            String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            String nomeNovoArquivo = UUID.randomUUID().toString() + extensao;

            // Resolve o caminho final e copia o arquivo para a pasta
            Path caminhoDestino = this.diretorioRaiz.resolve(nomeNovoArquivo).normalize().toAbsolutePath();

            try (InputStream inputStream = arquivo.getInputStream()) {
                Files.copy(inputStream, caminhoDestino, StandardCopyOption.REPLACE_EXISTING);
            }

            // Retorna apenas o nome do arquivo (ou um caminho relativo) para ser salvo no campo caminhoFoto do Aluno
            return nomeNovoArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Falha ao armazenar o arquivo.", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            Path arquivo = diretorioRaiz.resolve(nomeArquivo);
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao remover o arquivo " + nomeArquivo, e);
        }
    }
}