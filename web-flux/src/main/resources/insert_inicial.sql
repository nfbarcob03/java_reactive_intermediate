-- Clientes
INSERT INTO clientes (nombre, correo) VALUES
('Ana Torres', 'ana@example.com'),
('Luis Gómez', 'luis@example.com'),
('Marta Díaz', 'marta@example.com'),
('Carlos Ruiz', 'carlos@example.com');

-- Productos
INSERT INTO productos (nombre, precio) VALUES
('Laptop', 3500.00),
('Teclado', 150.00),
('Mouse', 90.00),
('Monitor', 800.00),
('Webcam', 120.00);

-- Órdenes
INSERT INTO ordenes (cliente_id, estado, fecha) VALUES
(1, 'ENTREGADA', '2025-06-01 10:00:00'),
(1, 'EN_PROCESO', '2025-06-02 12:00:00'),
(2, 'ENTREGADA', '2025-06-03 14:30:00'),
(2, 'CANCELADA', '2025-06-04 09:00:00'),
(3, 'ENTREGADA', '2025-06-05 11:15:00'),
(3, 'ENTREGADA', '2025-06-06 13:00:00'),
(3, 'ENTREGADA', '2025-06-07 16:45:00'),
(3, 'ENTREGADA', '2025-06-08 10:00:00'),
(3, 'ENTREGADA', '2025-06-09 08:00:00'),
(3, 'ENTREGADA', '2025-06-10 19:00:00'),
(3, 'ENTREGADA', '2025-06-11 20:00:00'),
(4, 'ENTREGADA', '2025-06-01 15:00:00');

-- Detalles de órdenes
INSERT INTO detalle_orden (orden_id, producto_id, cantidad) VALUES
(1, 1, 1),
(1, 2, 2),
(2, 3, 3),
(2, 4, 1),
(2, 5, 1),
(3, 2, 1),
(3, 3, 1),
(4, 1, 2),
(5, 1, 1),
(5, 2, 2),
(5, 3, 3),
(5, 4, 4),
(6, 3, 2),
(6, 4, 1),
(7, 1, 1),
(8, 2, 1),
(9, 2, 1),
(10, 2, 1),
(11, 2, 1);