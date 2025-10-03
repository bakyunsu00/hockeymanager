package org.example.domain;
//선수는 이름,나이,팀,포지션,등번호로 나뉜다
public class Player {

    private Long id;
    private String name;
    private int age;
    private Team team;
    private String pos;
    private int backNumber;


    public Player(String name, int age, Team team, String pos, int backNumber){
        this.name = name;
        this.age = age;
        this.team = team;
        this.pos = pos;
        this.backNumber = backNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Team getTeam() {
        return team;
    }

    public String getPos() {
        return pos;
    }

    public int getBackNumber() {
        return backNumber;
    }
}
