CREATE TABLE tb_enterprise (
    id BIGSERIAL PRIMARY KEY,
    enterprise VARCHAR(255) NOT NULL UNIQUE,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE tb_user (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enterprise_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (enterprise_id) REFERENCES tb_enterprise(id)
);

CREATE TABLE tb_benefit (
    id BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    supplier_enterprise_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (supplier_enterprise_id) REFERENCES tb_enterprise(id)
);

CREATE TABLE tb_partnership (
    id BIGSERIAL PRIMARY KEY,
    consumer_enterprise_id BIGINT NOT NULL,
    supplier_enterprise_id BIGINT NOT NULL,
    FOREIGN KEY (consumer_enterprise_id) REFERENCES tb_enterprise(id),
    FOREIGN KEY (supplier_enterprise_id) REFERENCES tb_enterprise(id)
);

CREATE TABLE tb_doubt (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    question TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

CREATE TABLE tb_answer (
    id BIGSERIAL PRIMARY KEY,
    doubt_id BIGINT NOT NULL,
    response TEXT,
    created_at TIMESTAMP,
    FOREIGN KEY (doubt_id) REFERENCES tb_doubt(id)
);
