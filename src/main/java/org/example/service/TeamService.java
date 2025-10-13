package org.example.service;

import org.example.dao.TeamDAO;
import org.example.dao.jdbc.JdbcTeamDao;
import org.example.domain.Team;

import java.util.List;

public class TeamService {

    TeamDAO teamDao = new JdbcTeamDao();

    public int save(Team team){
        return teamDao.insertTeam(team);
    }
    public int delete(Team team){
        return teamDao.deleteTeam(team);
    }
    public Team find(int id){
        return teamDao.selectTeam(id);
    }
    public Team findName(String name){
        return teamDao.selectTeamName(name);
    }
    public List<Team> findAll(){
        return teamDao.selectAll();
    }



}
