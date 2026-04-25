-- 1. Tabela de Usuários (Acesso ao Sistema)
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL, -- 'ADMIN', 'PROFESSOR'
    ativo BOOLEAN DEFAULT TRUE
    );

-- 2. Tabela de Planos (Precificação)
CREATE TABLE IF NOT EXISTS planos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL, -- Ex: 'Plano Mensal Adulto', 'Plano Trimestral'
    valor DECIMAL(10, 2) NOT NULL,
    frequencia_dias INTEGER DEFAULT 30, -- Para gerar cobranças automáticas a cada 30 dias
    ativo BOOLEAN DEFAULT TRUE
    );

-- 3. Tabela de Alunos (Agora conectada a um Plano)
CREATE TABLE IF NOT EXISTS alunos (
                                      id SERIAL PRIMARY KEY,
                                      nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    data_nascimento DATE,
    caminho_foto VARCHAR(255),
    status VARCHAR(20) DEFAULT 'ATIVO', -- 'ATIVO', 'INATIVO', 'AFASTADO'
    dia_vencimento INTEGER NOT NULL,
    plano_id INTEGER REFERENCES planos(id), -- Chave Estrangeira ligando ao Plano
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- 4. Tabela de Mensalidades (O coração financeiro)
CREATE TABLE IF NOT EXISTS mensalidades (
    id SERIAL PRIMARY KEY,
    aluno_id INTEGER REFERENCES alunos(id) ON DELETE CASCADE,
    referencia_mes_ano VARCHAR(7) NOT NULL, -- Ex: '04/2026'
    valor_cobrado DECIMAL(10, 2) NOT NULL,
    valor_pago DECIMAL(10, 2),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    status VARCHAR(20) DEFAULT 'PENDENTE', -- 'PENDENTE', 'PAGO', 'ATRASADO', 'CANCELADO'
    metodo_pagamento VARCHAR(50), -- 'PIX', 'DINHEIRO', 'CARTAO'
    observacao TEXT
    );

-- 5. Tabela de Histórico de Graduações (Evolução no Grupo)
CREATE TABLE IF NOT EXISTS historico_graduacao (
    id SERIAL PRIMARY KEY,
    aluno_id INTEGER REFERENCES alunos(id) ON DELETE CASCADE,
    graduacao VARCHAR(50) NOT NULL, -- Ex: 'Verde 1 volta'
    data_exame DATE NOT NULL,
    evento_local VARCHAR(100), -- Onde ocorreu a troca de corda
    observacao TEXT
    );