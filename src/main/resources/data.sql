-- Datos de ejemplo para Chaski Backend

-- Categorías
INSERT INTO categorias (nombre, imagen_url) VALUES
('Hamburguesas', 'https://example.com/categorias/hamburguesas.jpg'),
('Pizza', 'https://example.com/categorias/pizza.jpg'),
('Pollo', 'https://example.com/categorias/pollo.jpg'),
('Comida China', 'https://example.com/categorias/china.jpg'),
('Comida Criolla', 'https://example.com/categorias/criolla.jpg'),
('Sushi', 'https://example.com/categorias/sushi.jpg');

-- Restaurantes
INSERT INTO restaurantes (nombre, descripcion, imagen_logo_url, imagen_portada_url, direccion, calificacion_promedio, esta_abierto, tiempo_espera_minutos, costo_envio_base) VALUES
('Burger King', 'Las mejores hamburguesas de la ciudad', 'https://example.com/logos/bk.jpg', 'https://example.com/portadas/bk.jpg', 'Av. Larco 123, Miraflores', 4.5, true, 30, 5.00),
('Papa Johns', 'Pizza fresca y deliciosa', 'https://example.com/logos/pj.jpg', 'https://example.com/portadas/pj.jpg', 'Av. Benavides 456, Surco', 4.7, true, 40, 6.00),
('KFC', 'Pollo frito crujiente', 'https://example.com/logos/kfc.jpg', 'https://example.com/portadas/kfc.jpg', 'Av. Javier Prado 789, San Isidro', 4.3, true, 25, 4.50),
('Chifa Popular', 'Auténtica comida china', 'https://example.com/logos/chifa.jpg', 'https://example.com/portadas/chifa.jpg', 'Jr. Paruro 321, Lima', 4.2, false, 35, 5.50);

-- Relación Restaurante-Categorías
INSERT INTO restaurante_categorias (restaurante_id, categoria_id) VALUES
(1, 1), -- Burger King - Hamburguesas
(2, 2), -- Papa Johns - Pizza
(3, 3), -- KFC - Pollo
(4, 4); -- Chifa Popular - Comida China

-- Productos para Burger King
INSERT INTO productos (restaurante_id, nombre, descripcion, precio, imagen_url, disponible) VALUES
(1, 'Whopper', 'Hamburguesa clásica con carne a la parrilla', 15.90, 'https://example.com/productos/whopper.jpg', true),
(1, 'Whopper Jr', 'Versión pequeña del Whopper', 9.90, 'https://example.com/productos/whopper-jr.jpg', true),
(1, 'Papas Fritas Grandes', 'Papas fritas crujientes', 6.90, 'https://example.com/productos/papas.jpg', true);

-- Productos para Papa Johns
INSERT INTO productos (restaurante_id, nombre, descripcion, precio, imagen_url, disponible) VALUES
(2, 'Pizza Pepperoni Familiar', 'Pizza con pepperoni y queso', 39.90, 'https://example.com/productos/pepperoni.jpg', true),
(2, 'Pizza Hawaiana', 'Pizza con piña y jamón', 42.90, 'https://example.com/productos/hawaiana.jpg', true);

-- Productos para KFC
INSERT INTO productos (restaurante_id, nombre, descripcion, precio, imagen_url, disponible) VALUES
(3, 'Bucket 12 Piezas', '12 piezas de pollo frito', 59.90, 'https://example.com/productos/bucket.jpg', true),
(3, 'Combo Personal', '2 piezas de pollo + papas + gaseosa', 19.90, 'https://example.com/productos/combo.jpg', true);

-- Grupos de Opciones para Whopper
INSERT INTO grupos_opciones (producto_id, nombre, es_obligatorio, seleccion_minima, seleccion_maxima) VALUES
(1, 'Elige tu bebida', true, 1, 1),
(1, 'Extras', false, 0, 3);

-- Opciones para grupo "Elige tu bebida"
INSERT INTO opciones (grupo_opcion_id, nombre, precio_extra) VALUES
(1, 'Coca Cola', 0.00),
(1, 'Inca Kola', 0.00),
(1, 'Sprite', 0.00),
(1, 'Sin bebida', -3.00);

-- Opciones para grupo "Extras"
INSERT INTO opciones (grupo_opcion_id, nombre, precio_extra) VALUES
(2, 'Queso Extra', 2.50),
(2, 'Tocino', 3.50),
(2, 'Huevo', 2.00);

-- Usuario de ejemplo (password: "password123" hasheado con BCrypt)
INSERT INTO usuarios (nombre, email, passw, telefono, imagen_perfil_url, activo) VALUES
('Juan Pérez', 'juan@example.com', '$2a$10$rXQvXzYh/9dN0x9QZGt8kuKZ.8kJq8YqCpXqH9lYd7kXvPXQwPkxW', '987654321', 'https://example.com/usuarios/juan.jpg', true);

-- Direcciones de ejemplo
INSERT INTO direcciones (usuario_id, etiqueta, direccion_completa, referencia, latitud, longitud, es_predeterminada) VALUES
(1, 'Casa', 'Av. Lima 123, Dept 401', 'Edificio blanco, puerta negra', -12.046374, -77.042793, true),
(1, 'Trabajo', 'Av. Arequipa 456, Oficina 502', 'Torre empresarial', -12.093189, -77.034575, false);

