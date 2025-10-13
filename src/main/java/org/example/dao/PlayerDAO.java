package org.example.dao;

import org.example.domain.Player;

import java.util.List;

public interface PlayerDAO {


        int insertPlayer(Player player);
        Player selectPlayer(Long id);
        List<Player> selectAll();
        int deletePlayer(Player player);
        List<Player> findByTeamId(int id);

}
