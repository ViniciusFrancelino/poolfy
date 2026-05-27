-- =========================================================
-- DATABASE: DB CREATE
-- =========================================================
DROP DATABASE pool_system;
CREATE DATABASE pool_system;
USE pool_system;

-- =========================================================
-- TABELA: users
-- Responsável por autenticação, cadastro e perfil do usuário
-- =========================================================
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    user_type ENUM('PROFESSIONAL', 'COMMON') NOT NULL,
    experience_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================================================
-- TABELA: user_preferences
-- Preferências/configurações do usuário
-- =========================================================
CREATE TABLE user_preferences (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    preferred_user_type ENUM('PROFESSIONAL', 'COMMON') NULL,
    preferred_experience_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: pools
-- Cadastro das piscinas
-- =========================================================
CREATE TABLE pools (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    width_m DECIMAL(10,2) NULL,
    length_m DECIMAL(10,2) NULL,
    depth_m DECIMAL(10,2) NULL,
    volume_m3 DECIMAL(10,2) NOT NULL,
    shape ENUM('RECTANGULAR', 'ROUND', 'OVAL', 'IRREGULAR') NOT NULL,
    notes TEXT NULL,
    last_maintenance_date DATE NULL,
    next_maintenance_date DATE NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: maintenance_schedules
-- Agenda de manutenção
-- =========================================================
CREATE TABLE maintenance_schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pool_id INT NOT NULL,
    user_id INT NOT NULL,
    schedule_type ENUM('AUTOMATIC', 'MANUAL') NOT NULL,
    frequency_days INT NULL,
    next_scheduled_date DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    notes TEXT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (pool_id) REFERENCES pools(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: product_brands
-- Marcas cadastradas pelo usuário
-- =========================================================
CREATE TABLE product_brands (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    brand_name VARCHAR(120) NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    
);

-- =========================================================
-- TABELA: products
-- Cadastro de produtos/dosagens
-- =========================================================
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    brand_id INT NULL,
    name VARCHAR(150) NOT NULL,
    product_type ENUM(
        'CHLORINE',
        'ALGAECIDE',
        'CLARIFIER',
        'PH_INCREASER',
        'PH_REDUCER',
        'ALUMINUM_SULFATE',
        'OTHER'
    ) NOT NULL,
    dosage_per_m3 DECIMAL(10,4) NOT NULL,
    dosage_unit ENUM('G', 'KG', 'ML', 'L') NOT NULL,
    description TEXT NULL,
    low_stock_alert_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    low_stock_threshold DECIMAL(10,2) NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, 
    FOREIGN KEY (brand_id) REFERENCES product_brands(id) ON DELETE SET NULL
);

-- =========================================================
-- TABELA: inventory
-- Estoque atual por produto
-- =========================================================
CREATE TABLE inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    current_quantity DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    unit ENUM('G', 'KG', 'ML', 'L') NOT NULL,
    
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: inventory_movements
-- Entrada e saída de estoque
-- =========================================================
CREATE TABLE inventory_movements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    inventory_id INT NOT NULL,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    movement_type ENUM('IN', 'OUT') NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit ENUM('G', 'KG', 'ML', 'L') NOT NULL,
    reason VARCHAR(255) NULL,
    maintenance_id INT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: maintenances
-- Registro de manutenção
-- =========================================================
CREATE TABLE maintenances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pool_id INT NOT NULL,
    user_id INT NOT NULL,
    maintenance_date DATE NOT NULL,
    maintenance_type ENUM('CLEANING', 'CHEMICAL_TREATMENT', 'COMPLETE') NOT NULL,
    description TEXT NULL,
    next_maintenance_date DATE NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (pool_id) REFERENCES pools(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: maintenance_products
-- Produtos usados em cada manutenção
-- Aqui fica salvo o cálculo feito com base no volume da piscina
-- =========================================================
CREATE TABLE maintenance_products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    maintenance_id INT NOT NULL,
    product_id INT NOT NULL,
    pool_volume_m3 DECIMAL(10,2) NOT NULL,
    dosage_per_m3 DECIMAL(10,4) NOT NULL,
    dosage_unit ENUM('G', 'KG', 'ML', 'L') NOT NULL,
    calculated_quantity DECIMAL(10,2) NOT NULL,
    applied_quantity DECIMAL(10,2) NULL,
    notes TEXT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (maintenance_id) REFERENCES maintenances(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- =========================================================
-- TABELA: alerts
-- Alertas do sistema, como estoque baixo
-- =========================================================
CREATE TABLE alerts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NULL,
    pool_id INT NULL,
    alert_type ENUM('LOW_STOCK', 'PENDING_MAINTENANCE', 'UPCOMING_MAINTENANCE') NOT NULL,
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, 
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE SET NULL, 
    FOREIGN KEY (pool_id) REFERENCES pools(id) ON DELETE SET NULL
);

-- =========================================================
-- TABELA: recent_activities
-- Atividades recentes para o dashboard
-- =========================================================
CREATE TABLE recent_activities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    activity_type ENUM(
        'POOL_CREATED',
        'POOL_UPDATED',
        'POOL_DELETED',
        'MAINTENANCE_CREATED',
        'PRODUCT_CREATED',
        'PRODUCT_UPDATED',
        'STOCK_IN',
        'STOCK_OUT'
    ) NOT NULL,
    reference_id INT NULL,
    description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- =========================================================
-- Adição posterior da FK circular de inventory_movements -> maintenances
-- Feita depois porque inventory_movements foi criada antes
-- =========================================================
ALTER TABLE inventory_movements ADD FOREIGN KEY (maintenance_id) REFERENCES maintenances(id) ON DELETE SET NULL;