/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package logica.Interfaces;

import logica.Clases.Categoria;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UnwantedOpinion
 */
public interface IControladorCategoria {
    public abstract boolean altaCategoria(Categoria categoria);
    public abstract boolean modificarCategoria(int id, Categoria categoria);
    public abstract boolean deshabilitarCategoria(int id);
    public abstract ArrayList<Categoria> listarCategorias();
    public abstract ArrayList<Categoria> listarCategoriasActivas();
    public abstract Categoria buscarCategoria(int id);
    public abstract List<String> obtenerNombresCategorias();
    public abstract Categoria buscarCategoriaPorNombre(String nombre);
    public abstract boolean categoriaTieneProductos(int categoriaId);
}
