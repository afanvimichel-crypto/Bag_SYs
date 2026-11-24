CREATE TABLE roles (
    id CHAR(36) NOT NULL,
    code VARCHAR(50) NOT NULL,
    label VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_roles_code UNIQUE (code)
) ENGINE=InnoDB;

CREATE TABLE users (
    id CHAR(36) NOT NULL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(180) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE user_roles (
    user_id CHAR(36) NOT NULL,
    role_id CHAR(36) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id)
        REFERENCES roles (id) ON DELETE RESTRICT
) ENGINE=InnoDB;

INSERT INTO roles (id, code, label, description) VALUES
    ('8e23cba2-e772-45e3-9db6-aee31c34c5b0', 'ADMIN', 'Administrateur', 'Accès complet au système'),
    ('1d2b9889-9908-4a14-92d4-eb04fce39038', 'MANAGER', 'Responsable', 'Supervision des opérations'),
    ('cb8f8274-98aa-4146-87cf-588b38a7475a', 'RECEPTION_AGENT', 'Agent de réception', 'Gestion des dépôts'),
    ('ff06f2ba-55a0-4a54-a2eb-5ac96eb46c54', 'WITHDRAWAL_AGENT', 'Agent de retrait', 'Validation des retraits'),
    ('3e71a291-e4e1-4ad5-8c12-2c8664a9eb07', 'ACCOUNTANT', 'Comptable', 'Gestion des paiements et rapports financiers');
