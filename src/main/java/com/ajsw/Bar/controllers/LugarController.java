package com.ajsw.Bar.controllers;

import com.ajsw.Bar.models.Lugar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LugarController {
    private static final double RADIO_TIERRA_METROS = 6371000.0; // Radio de la Tierra en metros
    // Supongamos que tienes una lista de lugares
    private List<Lugar> listaDeLugares = new ArrayList<>();
    @Autowired
    private JdbcTemplate jdbcTemplate; // o tu repositorio JPA si lo estás utilizando

    // Constructor para inicializar algunos lugares (puedes agregar más)
    public LugarController() {
        listaDeLugares.add(new Lugar("Lugar1", 40.7128, -74.0060));
        listaDeLugares.add(new Lugar("Lugar2", 34.0522, -118.2437));
        // Agregar más lugares aquí...
    }

    @GetMapping
    public ResponseEntity<String> testConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return ResponseEntity.ok("Conexión exitosa a la base de datos.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en la conexión a la base de datos: " + e.getMessage());
        }
    }

    @GetMapping("/buscar-lugares")
    public List<Lugar> buscarLugaresEnRadio(
            @RequestParam("latitud") double latitud,
            @RequestParam("longitud") double longitud,
            @RequestParam("radio") double radio) {

        List<Lugar> lugaresEnRadio = new ArrayList<>();

        for (Lugar lugar : listaDeLugares) {
            double distancia = calcularDistanciaEnMetros(latitud, longitud, lugar.getLatitud(), lugar.getLongitud());

            // Si la distancia es menor o igual al radio predefinido, el lugar está dentro del radio
            if (distancia <= radio) {
                lugaresEnRadio.add(lugar);
            }
        }

        return lugaresEnRadio;
    }

    // Método para calcular la distancia entre dos puntos geográficos (fórmula de Haversine)
    private double calcularDistanciaEnMetros(double latitud1, double longitud1, double latitud2, double longitud2) {
        // Convierte las latitudes y longitudes de grados a radianes
        double latitud1Rad = Math.toRadians(latitud1);
        double longitud1Rad = Math.toRadians(longitud1);
        double latitud2Rad = Math.toRadians(latitud2);
        double longitud2Rad = Math.toRadians(longitud2);

        // Diferencia de latitudes y longitudes
        double latitudDif = latitud2Rad - latitud1Rad;
        double longitudDif = longitud2Rad - longitud1Rad;

        // Fórmula de Haversine
        double a = Math.pow(Math.sin(latitudDif / 2), 2) + Math.cos(latitud1Rad) * Math.cos(latitud2Rad) * Math.pow(Math.sin(longitudDif / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia en metros
        double distanciaMetros = RADIO_TIERRA_METROS * c;

        return distanciaMetros;
    }
}

