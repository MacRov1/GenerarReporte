/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica.Controladores;

import logica.Clases.Categoria;
import logica.Interfaces.IControladorCategoria;
import logica.servicios.CategoriaServicios;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author UnwantedOpinion
 */
public class ControladorCategoria implements IControladorCategoria {

    private static ControladorCategoria instancia;
    private CategoriaServicios categoriaServicios;

    private ControladorCategoria() {
        categoriaServicios = new CategoriaServicios();
    }

    public static ControladorCategoria getInstance() {
        if (instancia == null) {
            instancia = new ControladorCategoria();
        }
        return instancia;
    }

    @Override
    public boolean altaCategoria(Categoria categoria) {
        return categoriaServicios.altaCategoria(categoria);
    }

    @Override
    public boolean modificarCategoria(int id, Categoria categoria) {
        return categoriaServicios.modificarCategoria(id, categoria);
    }

    @Override
    public boolean deshabilitarCategoria(int id) {
        return categoriaServicios.deshabilitarCategoria(id);
    }

    @Override
    public ArrayList<Categoria> listarCategorias() {
        return categoriaServicios.listarCategorias();
    }

    @Override
    public ArrayList<Categoria> listarCategoriasActivas(){
        return categoriaServicios.listarCategoriasActivas();
    }
    
    @Override
    public Categoria buscarCategoria(int id) {
        return categoriaServicios.buscarCategoria(id);
    }
    
    @Override
    public List<String> obtenerNombresCategorias(){
        return categoriaServicios.obtenerNombresCategorias();
    }
    
    @Override
    public Categoria buscarCategoriaPorNombre(String nombre){
        return categoriaServicios.buscarCategoriaPorNombre(nombre);
    }
    
    @Override
    public boolean categoriaTieneProductos(int categoriaId){
        return categoriaServicios.categoriaTieneProductos(categoriaId);
    }
}
