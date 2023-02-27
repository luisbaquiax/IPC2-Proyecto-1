/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.baquiax.tienda.db.modelo;

import com.baquiax.tienda.db.coneccion.ConeccionDB;
import com.baquiax.tienda.entidad.Tienda;
import com.baquiax.tienda.entidad.UsuarioBodega;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public class TiendaDB {

    private final static String INSERT = "INSERT INTO tienda(codigo,direccion,tipo) VALUES(?,?,?)";
    private final static String UPDATE = "UPDATE tienda SET direccion = ?, tipo = ? WHERE codigo = ?";
    private final static String SELECT = "SELECT * FROM tienda";
    /**
     * Tiendas por usuario bodega
     */
    private final static String SELECT_BY_USUARIO_BODEGA
            = "SELECT t.codigo, t.tipo, t.direccion\n"
            + "FROM tienda t\n"
            + "RIGHT JOIN bodega_tienda b\n"
            + "ON  t.codigo = b.codigo_tienda WHERE b.codigo_usuario_bodega = ?";

    private ResultSet resultSet;

    public TiendaDB() {
    }

    /**
     *
     * @param tienda
     * @return
     */
    public boolean insert(Tienda tienda) {
        try (PreparedStatement statement = ConeccionDB.getConnection().prepareStatement(INSERT)) {
            statement.setString(1, tienda.getCodigo());
            statement.setString(2, tienda.getDireccion());
            statement.setString(3, tienda.getTipo());
            statement.executeUpdate();

            statement.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param tienda
     * @return
     */
    public boolean update(Tienda tienda) {
        try (PreparedStatement statement = ConeccionDB.getConnection().prepareStatement(UPDATE)) {
            statement.setString(1, tienda.getDireccion());
            statement.setString(2, tienda.getTipo());
            statement.setString(3, tienda.getCodigo());
            statement.executeUpdate();

            statement.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     *
     * @return
     */
    public List<Tienda> getTienda() {
        List<Tienda> tiendas = new ArrayList<>();
        try (PreparedStatement statement = ConeccionDB.getConnection().prepareStatement(SELECT)) {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tiendas.add(getTienda(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return tiendas;
    }

    /**
     * LISTA LAS TIENDAS DE UN USUARIO DE BODEGA
     *
     * @param usuarioBodega
     * @return
     */
    public ArrayList<Tienda> getTiendaByUsuarioBodega(UsuarioBodega usuarioBodega) {
        List<Tienda> tiendas = new ArrayList<>();
        try (PreparedStatement statement = ConeccionDB.getConnection().prepareStatement(SELECT_BY_USUARIO_BODEGA)) {
            statement.setString(1, usuarioBodega.getCodigo());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tiendas.add(getTienda(resultSet));
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return (ArrayList<Tienda>) tiendas;
    }

    private Tienda getTienda(ResultSet resultSet) throws SQLException {
        return new Tienda(
                resultSet.getString("codigo"),
                resultSet.getString("direccion"),
                resultSet.getString("tipo"));
    }
}
