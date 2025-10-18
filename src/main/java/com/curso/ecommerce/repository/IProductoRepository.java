package com.curso.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.curso.ecommerce.model.Producto;

//1.LLAMAMOS LOS METODOS DE JpaRepository, E INDICAMOS A QUIEN PERTENECE,
@Repository
//se implementaran los metodos de JpaRepository a la clase Producto e integer es el tipo de dato del id
public interface IProductoRepository extends JpaRepository<Producto, Integer> {

}
