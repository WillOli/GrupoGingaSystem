// 1. LÓGICA DO TEMA (Fica fora, para funcionar assim que abrir a página)
const themeToggle = document.getElementById('themeToggle');
const body = document.body;

if (themeToggle) { // Verifica se o botão existe para não dar erro
    themeToggle.addEventListener('click', () => {
        if (body.classList.contains('theme-claro')) {
            body.classList.replace('theme-claro', 'theme-azul');
            themeToggle.innerHTML = '<i class="bi bi-sun-fill"></i> Modo Claro';
        } else {
            body.classList.replace('theme-azul', 'theme-claro');
            themeToggle.innerHTML = '<i class="bi bi-moon-stars-fill"></i> Modo Azul';
        }
    });
}

// 2. LÓGICA DO LOGIN
document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const username = document.getElementById('username').value; // Mudei de 'email' para 'username' conforme o HTML
    const senha = document.getElementById('password').value;
    const errorAlert = document.getElementById('loginError');

    // Validação visual simples
    if (username === '' || senha === '') {
        alert('Por favor, preencha todos os campos.');
        return;
    }

    console.log('Tentando logar com:', username);

    // --- TESTE COM O BACKEND (DESCOMENTADO E AJUSTADO) ---
    // Vamos usar o endpoint que o Spring Security cria por padrão ou o nosso futuro controller
    fetch('/api/login', { // Rota que vamos criar
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            senha: senha
        })
    })
        .then(response => {
            if (response.ok) {
                window.location.href = 'index.html'; // Sucesso!
            } else {
                alert('Usuário ou senha inválidos!');
            }
        })
        .catch(error => {
            console.error('Erro na requisição:', error);
            // Por enquanto, como o controller não existe, vai cair aqui.
            // Vamos manter a simulação para você não travar:
            alert('Modo Simulação: Login realizado (Backend ainda offline)');
            window.location.href = 'index.html';
        });
});