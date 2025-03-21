CREATE TABLE IF NOT EXISTS jogadores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    tipo VARCHAR(20) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro VARCHAR(30) NOT NULL
    );

CREATE TABLE IF NOT EXISTS pagamentos (
    id SERIAL PRIMARY KEY,
    jogador_id INT REFERENCES jogadores(id),
    valor DOUBLE PRECISION NOT NULL,
    data_pagamento TIMESTAMP NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    mes_referencia VARCHAR(7),
    status VARCHAR(20) DEFAULT 'PENDENTE'
    );

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'JOGADOR'
    );
