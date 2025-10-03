package org.example.service;

import org.example.dao.PlayerDAO;
import org.example.dao.jdbc.jdbcPlayerDao;
import org.example.domain.Player;

import java.util.List;

public class PlayerService {

    PlayerDAO playerDAO = new jdbcPlayerDao();

    //CRUD, 등번호 검증,
    public void save(Player player){
        playerDAO.insertPlayer(player);
    }

    public Player findById(Long id){
        Player player = playerDAO.selectPlayer(id);

        return player;
    }

    public List<Player> findAll(){
        return playerDAO.selectAll();
    }

    public int delete(Player player){
        return playerDAO.deletePlayer(player);
    }

}
