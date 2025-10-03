package org.example.dao;


import org.example.domain.Team;

import java.util.List;

public interface TeamDAO {


    public int insertTeam(Team team);
    public int deleteTeam(Team team);
    public Team selectTeam(int id);
    public List<Team> selectAll();





}
