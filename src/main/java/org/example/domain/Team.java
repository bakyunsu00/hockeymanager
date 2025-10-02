package org.example.domain;


//팀은 팀명,소속지역,인원수로 나뉜다.
public class Team {

    private String teamName;
    private String location;
    private int playerNums;


    public Team(String teamName, String location, int playerNums) {
        this.teamName = teamName;
        this.location = location;
        this.playerNums = playerNums;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getLocation() {
        return location;
    }

    public int getPlayerNums() {
        return playerNums;
    }
}
