package org.example.domain;


//팀은 팀명,소속지역,인원수로 나뉜다.
public class Team {

    private int id;
    private String teamName;
    private String location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Team(int id, String teamName, String location) {
        this.id = id;
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
