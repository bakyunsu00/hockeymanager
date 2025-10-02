package org.example.domain;


//팀은 팀명,소속지역,인원수로 나뉜다.
public class Team {

    private int id;
    private String teamName;
    private String location;



    public Team(String teamName, String location) {
        this.teamName = teamName;
        this.location = location;

    }

    public String getTeamName() {
        return teamName;
    }

    public String getLocation() {
        return location;
    }

}
