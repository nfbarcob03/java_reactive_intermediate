package com.example.webfluxintro.service;

import com.example.webfluxintro.dto.ProductoVentasDTO;
import com.example.webfluxintro.model.*;
import com.example.webfluxintro.repository.ClienteRepository;
import com.example.webfluxintro.repository.DetalleOrdenRepository;
import com.example.webfluxintro.repository.OrdenRepository;
import com.example.webfluxintro.repository.ProductoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

@Service
public class ConsultaService {
 
    private final ClienteRepository clienteRepo;
    private final OrdenRepository ordenRepo;
    private final DetalleOrdenRepository detalleRepo;
    private final ProductoRepository productoRepo;
    private final DatabaseClient dbClient;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductoEventBus eventBus;



    public ConsultaService(ClienteRepository clienteRepo, OrdenRepository ordenRepo,
                           DetalleOrdenRepository detalleRepo, ProductoRepository productoRepo, DatabaseClient dbClient, ApplicationEventPublisher eventPublisher, ProductoEventBus eventBus) {
        this.clienteRepo = clienteRepo;
        this.ordenRepo = ordenRepo;
        this.detalleRepo = detalleRepo;
        this.productoRepo = productoRepo;
        this.dbClient = dbClient;
        this.eventPublisher = eventPublisher;
        this.eventBus = eventBus;
    }

    // 1. Órdenes realizadas por cliente en un rango de fechas
    public Flux<Orden> ordenesRealizadasPorFecha(Long idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        Objects.requireNonNull(idCliente, "El id del cliente no puede ser nulo");
        return ordenRepo.findAll()
                .filter(orden -> idCliente.equals(orden.getClienteId()))
                .filter(orden -> {
                    LocalDateTime fechaOrden = orden.getFecha();
                    return (fechaOrden.isEqual(fechaInicio) || fechaOrden.isAfter(fechaInicio)) &&
                           (fechaOrden.isEqual(fechaFin) || fechaOrden.isBefore(fechaFin));
                });
    }

    // 2. Órdenes con estado específico
    public Flux<Orden> ordenesConEstadoEspecifico(String estado){
        return ordenRepo.findAll()
                .filter(orden -> estado.equalsIgnoreCase(orden.getEstado()));
    }

    // 4. Total de ventas por cliente (simulación)
    public Flux<String> totalDeVentaOrdenes(){
        return ordenRepo.findAll()
                .flatMap(orden -> detalleRepo.findByOrdenId(orden.getId())
                        .flatMap(detalle -> productoRepo.findById(detalle.getProductoId())
                                .map(producto -> new Object[]{
                                        orden.getClienteId(), producto.getPrecio()* detalle.getCantidad()
                                }))).collectMultimap( objects -> (Long) objects[0], objects -> (Double) objects[1])
                .flatMapMany(longCollectionMap -> Flux.fromIterable(longCollectionMap.entrySet())
                        .map(entidad->"Cliente: "  + entidad.getKey() +
                                " - Total Venta: $" + entidad.getValue().stream()
                                        .mapToDouble(Double::doubleValue).sum()));

    }

    // 3. Productos más comprados (simulación simple)
    public Flux<ProductoVentasDTO> findProductosMasComprados() {
        return detalleRepo.findAll()
                .groupBy(DetalleOrden::getProductoId)
                .flatMap(group -> group
                        .map(DetalleOrden::getCantidad)
                        .reduce(0, Integer::sum)
                        .flatMap(total ->
                                productoRepo.findById(group.key())
                                        .map(producto -> new ProductoVentasDTO(producto.getNombre(), total))
                        )
                )
                .sort((a, b) -> b.totalVendido().compareTo(a.totalVendido()));
    }

    public Flux<ProductoVentasDTO> findProductosMasCompradosFiltrados(int page, int size, String sortDir, String keyword) {
        Comparator<ProductoVentasDTO> comparator = Comparator.comparing(ProductoVentasDTO::totalVendido);
        if(!"asc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }
        return detalleRepo.findAll()
                .groupBy(DetalleOrden::getProductoId)
                .flatMap(group -> group
                        .map(DetalleOrden::getCantidad)
                        .reduce(0, Integer::sum)
                        .flatMap(total ->
                                productoRepo.findById(group.key())
                                        .map(producto -> new ProductoVentasDTO(producto.getNombre(), total))
                        )
                )
                .filter(dto->{
                    return dto.nombre().toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
                })
                .sort(comparator).skip( (long) page * size).take(size);
    }
    // 5. Total de ventas por producto
    public Flux<String> totalVentasPorProducto() {
        return detalleRepo.findAll()
                .flatMap(detalle -> productoRepo.findById(detalle.getProductoId())
                        .map(producto -> new Object[]{producto.getNombre(), producto.getPrecio() * detalle.getCantidad()}))
                .collectMultimap(obj -> (String) obj[0], obj -> (Double) obj[1])
                .flatMapMany(map -> Flux.fromIterable(map.entrySet())
                        .map(entry -> "Producto " + entry.getKey() + " Total: " + entry.getValue().stream().mapToDouble(Double::doubleValue).sum()));
    }

    // 6. Órdenes con más de 3 productos distintos
    public Flux<Orden> ordenesConMasDeTresProductos() {
        return ordenRepo.findAll()
                .flatMap(orden -> detalleRepo.findByOrdenId(orden.getId())
                        .map(DetalleOrden::getProductoId)
                        .distinct()
                        .count()
                        .filter(count -> count > 3)
                        .map(c -> orden));
    }

    // 7. Clientes con más de 5 órdenes
    public Flux<Cliente> clientesConMasDeCincoOrdenes() {
        return ordenRepo.findAll()
                .groupBy(Orden::getClienteId)
                .flatMap(group -> group.count().filter(count -> count > 5).map(c -> group.key()))
                .flatMap(clienteRepo::findById);
    }

    // 8. Órdenes sin productos asociados
    public Flux<Orden> ordenesSinProductos() {
        return ordenRepo.findAll()
                .flatMap(orden -> detalleRepo.findByOrdenId(orden.getId()).hasElements()
                        .filter(has -> !has)
                        .map(b -> orden));
    }

    // 9. Órdenes y detalles de cliente por ID de orden
    public Mono<String> ordenYDetallesPorId(Long ordenId) {
        return ordenRepo.findById(ordenId)
                .flatMapMany(orden -> detalleRepo.findByOrdenId(orden.getId())
                        .flatMap(detalle -> productoRepo.findById(detalle.getProductoId())
                                .map(prod -> "Producto: " + prod.getNombre() + " x" + detalle.getCantidad())))
                .collectList()
                .map(lista -> "Orden " + ordenId + ": " + String.join(", ", lista));
    }

    // 10. Clientes que no han hecho ninguna orden
    public Flux<Cliente> clientesSinOrdenes() {
        return clienteRepo.findAll()
                .flatMap(cliente -> ordenRepo.findAll()
                        .filter(o -> o.getClienteId().equals(cliente.getId()))
                        .hasElements()
                        .filter(b -> !b)
                        .map(v -> cliente));
    }

    public Flux<ProductoVentasDTO> getProductosMasCompradosQuery(
            int page, int size, String sortDir, String keyword) {

        String sortOrder = sortDir.equalsIgnoreCase("asc") ? "ASC" : "DESC";
        int offset = page * size;

        String sql = """
            SELECT p.nombre, SUM(d.cantidad) AS total_vendido
            FROM detalle_orden d
            JOIN productos p ON p.id = d.producto_id
            WHERE LOWER(p.nombre) LIKE :keyword
            GROUP BY p.nombre
            ORDER BY total_vendido """ + " " + sortOrder +"""
            LIMIT :limit OFFSET :offset
        """;

        return dbClient.sql(sql)
                .bind("keyword", "%" + keyword.toLowerCase() + "%")
                .bind("limit", size)
                .bind("offset", offset)
                .map(row -> new ProductoVentasDTO(
                        row.get("nombre", String.class),
                        row.get("total_vendido", Integer.class)))
                .all();
    }

    public Mono<Producto> create(Producto producto) {
        return productoRepo.save(producto)
                .doOnSuccess(savedProducto -> {
                    // Publicar un evento después de guardar el producto
                    eventPublisher.publishEvent(new ProductoEvento("creado", savedProducto));
                });
    }

    // hacer el mismo service pero para update
    public Mono<Producto> update(Producto producto) {
        return productoRepo.save(producto)
                .doOnSuccess(p -> eventBus.publicarEvento(new ProductoEvento("CREADO", p)));
    }

}