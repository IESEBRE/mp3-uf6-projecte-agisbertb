package org.example.model.daos;

import org.example.model.exceptions.DAOException;
import java.util.List;

/**
 * Interfície genèrica per a l'accés a dades (DAO).
 *
 * Defineix les operacions bàsiques d'accés a dades que es poden realitzar en una base de dades,
 * incloent operacions de recuperació, guardat, actualització i eliminació d'entitats.
 *
 * @param <T> El tipus d'entitat sobre la qual opera el DAO.
 */

public interface DAO <T>{

    /**
     * Recupera una entitat per la seva clau primària.
     *
     * @param id La clau primària de l'entitat a recuperar.
     * @return L'entitat trobada o null si no es troba cap.
     * @throws DAOException Si ocorre algun error durant l'operació.
     */

    T get(Long id) throws DAOException;

    /**
     * Recupera totes les entitats d'un tipus específic.
     *
     * @return Una llista de totes les entitats.
     * @throws DAOException Si ocorre algun error durant l'operació.
     */

    List<T> getAll() throws DAOException;

    /**
     * Guarda una nova entitat a la base de dades.
     *
     * @param obj L'entitat a guardar.
     * @throws DAOException Si ocorre algun error durant l'operació de guardat.
     */

    void save(T obj) throws DAOException;

    /**
     * Actualitza una entitat existent a la base de dades.
     *
     * @param obj L'entitat a actualitzar.
     * @throws DAOException Si ocorre algun error durant l'operació d'actualització.
     */

    void update(T obj) throws DAOException;

    /**
     * Elimina una entitat de la base de dades utilitzant la seva clau primària.
     *
     * @param id La clau primària de l'entitat a eliminar.
     * @throws DAOException Si ocorre algun error durant l'operació d'eliminació.
     */

    void delete(Long id) throws DAOException;
}