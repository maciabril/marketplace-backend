-- =========================================
-- Script para agregar las 7 nuevas columnas a la tabla orders
-- =========================================

ALTER TABLE orders 
ADD COLUMN order_date TIMESTAMP,
ADD COLUMN phone VARCHAR(100),
ADD COLUMN address VARCHAR(500),
ADD COLUMN address_line2 VARCHAR(500),
ADD COLUMN city VARCHAR(100),
ADD COLUMN postal_code VARCHAR(20),
ADD COLUMN payment_method VARCHAR(50);

-- Actualizar registros existentes con valores por defecto (si los hay)
UPDATE orders 
SET 
  order_date = CURRENT_TIMESTAMP,
  phone = 'No especificado',
  address = 'No especificado',
  city = 'No especificado',
  postal_code = '0000',
  payment_method = 'TARJETA'
WHERE order_date IS NULL;

-- Hacer obligatorios los campos necesarios (excepto addressLine2 que es opcional)
ALTER TABLE orders 
MODIFY COLUMN order_date TIMESTAMP NOT NULL,
MODIFY COLUMN phone VARCHAR(100) NOT NULL,
MODIFY COLUMN address VARCHAR(500) NOT NULL,
MODIFY COLUMN city VARCHAR(100) NOT NULL,
MODIFY COLUMN postal_code VARCHAR(20) NOT NULL,
MODIFY COLUMN payment_method VARCHAR(50) NOT NULL;
