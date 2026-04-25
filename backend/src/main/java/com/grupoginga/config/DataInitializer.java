package com.grupoginga.config;


import com.grupoginga.repository.UsuarioRepository;
import com.grupoginga.model.Usuario;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Checa se a tabela de usuários está vazia
        if (usuarioRepository.count() == 0) {

            Usuario admin = new Usuario();
            admin.setEmail("admin@grupoginga.com.br");

            // Aqui entra a criptografia com BCrypt na senha padrão inicial
            admin.setSenhaHash(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");

            usuarioRepository.save(admin);

            System.out.println("✅ PRIMEIRO ACESSO: Usuário Admin criado com sucesso!");
            System.out.println("Email: admin@grupoginga.com.br | Senha: admin123");
        } else {
            System.out.println("👍 Banco já possui usuários. Setup de admin ignorado.");
        }
    }
}