package org.example.dao.jdbc;

import org.example.config.DataSource;
import org.example.dao.PlayerDAO;
import org.example.domain.Player;
import org.example.domain.Team;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class jdbcPlayerDao implements PlayerDAO {
    @Override
    public int insertPlayer(Player player){
        int ret = -1;


        String insertSql = "INSERT INTO player(player_id,player_name,player_age,player_team_id,player_position,player_backnumber)"+
                " VALUES(?,?,?,?,?,?);";


        try(Connection con = DataSource.getConnection();
            PreparedStatement pstm = con.prepareStatement(insertSql);)
        {
            pstm.setLong(1, player.getId());
            pstm.setString(2, player.getName());
            pstm.setInt(3, player.getAge());
            pstm.setInt(4, player.getTeam().getId());
            pstm.setString(5, player.getPos());
            pstm.setInt(6,player.getBackNumber());
            ret = pstm.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }





        return ret;
    }

    @Override
    public Player selectPlayer(Long id) {

        String selectSql = "SELECT * FROM player WHERE player_id = ?";
        Player player = null;

        try(Connection con = DataSource.getConnection();
            PreparedStatement pstm = con.prepareStatement(selectSql);) {
            pstm.setLong(1, id);

            try (ResultSet rs = pstm.executeQuery();) {
                if (rs.next()) {
                    Long playerId = rs.getLong("player_id");
                    String playerName = rs.getString("player_name");
                    int playerAge = rs.getInt("player_age");
                    String playerPos = rs.getString("player_position");
                    int playerBackNumber = rs.getInt("player_backnumber");


                    Integer playerTeamId = rs.getObject("player_team_id", Integer.class);
                    Team playerTeam = null;

                    if (playerTeamId != null) {
                        try (PreparedStatement pstm2 = con.prepareStatement("SELECT * FROM team WHERE team_id = ?")) {
                            pstm2.setInt(1, playerTeamId);
                            try (ResultSet rs2 = pstm2.executeQuery();) {
                                if (rs2.next()) {
                                    playerTeam = new Team(rs2.getInt("team_id"), rs2.getString("team_name"), rs2.getString("team_location"));
                                }

                            }
                            player = new Player(playerId, playerName, playerAge, playerTeam, playerPos, playerBackNumber);


                        }
                    }
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return player;
    }

    @Override
    public List<Player> selectAll() {

        String selectSql = "SELECT * FROM player";
        Player player = null;
        List<Player> playerList = new ArrayList<>();

        try(Connection con = DataSource.getConnection();
            PreparedStatement pstm = con.prepareStatement(selectSql);) {

            try (ResultSet rs = pstm.executeQuery();) {
                while (rs.next()) {
                    Long playerId = rs.getLong("player_id");
                    String playerName = rs.getString("player_name");
                    int playerAge = rs.getInt("player_age");
                    String playerPos = rs.getString("player_position");
                    int playerBackNumber = rs.getInt("player_backnumber");


                    Integer playerTeamId = rs.getObject("player_team_id", Integer.class);
                    Team playerTeam = null;

                    if (playerTeamId != null) {
                        try (PreparedStatement pstm2 = con.prepareStatement("SELECT * FROM team WHERE team_id = ?")) {
                            pstm2.setInt(1, playerTeamId);
                            try (ResultSet rs2 = pstm2.executeQuery();) {
                                if (rs2.next()) {
                                    playerTeam = new Team(rs2.getInt("team_id"), rs2.getString("team_name"), rs2.getString("team_location"));
                                }

                            }
                            player = new Player(playerId, playerName, playerAge, playerTeam, playerPos, playerBackNumber);


                        }
                    }
                    playerList.add(player);
                }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return playerList;
    }

    @Override
    public int deletePlayer(Player player) {
        String deleteSql = "DELETE FROM player WHERE player_id = ?";
        int result = -1;
        try(Connection con = DataSource.getConnection();
        PreparedStatement pstm = con.prepareStatement(deleteSql)){
            pstm.setLong(1,player.getId());
            result =  pstm.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();

        }
        return result;
    }
}
