package org.example.dao.jdbc;

import org.example.config.DataSource;
import org.example.dao.TeamDAO;
import org.example.domain.Team;

import javax.xml.crypto.Data;
import java.awt.image.DataBufferShort;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTeamDao implements TeamDAO {


    @Override
    public int insertTeam(Team team) {
        String insertSql = "INSERT INTO team VALUES(?,?,?)";
        int ret = -1;
        try(Connection con = DataSource.getConnection();
            PreparedStatement pstm = con.prepareStatement(insertSql);)
        {
            pstm.setInt(1,team.getId());
            pstm.setString(2,team.getTeamName());
            pstm.setString(3,team.getLocation());
            ret = pstm.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public int deleteTeam(Team team) {

        String deleteSql = "DELETE FROM team WHERE team_id = ?";
        int ret = -1;
        try(Connection con = DataSource.getConnection();
        PreparedStatement pstm = con.prepareStatement(deleteSql);)
        {
            pstm.setInt(1,team.getId());
            ret = pstm.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }



        return ret;
    }

    @Override
    public Team selectTeam(int id) {

        String selectSql = "SELECT * FROM team WHERE team_id = ?";
        Team team = null;
        try (Connection con = DataSource.getConnection();
             PreparedStatement pstm = con.prepareStatement(selectSql);) {

            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery();) {
                if (rs.next()) {
                    team = new Team(rs.getInt("team_id"), rs.getString("team_name"), rs.getString("team_location"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return team;
    }


    @Override
    public List<Team> selectAll() {
        String selectSql = "SELECT * FROM team";
        Team team = null;
        List<Team> teamList = new ArrayList<>();
        try (Connection con = DataSource.getConnection();
             PreparedStatement pstm = con.prepareStatement(selectSql);) {
            try (ResultSet rs = pstm.executeQuery();) {
                while(rs.next()) {
                    team = new Team(rs.getInt("team_id"), rs.getString("team_name"), rs.getString("team_location"));
                    teamList.add(team);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamList;
    }
}
