package org.example.domain;
//선수는 이름,나이,팀,포지션,등번호로 나뉜다
public class Player {

    private long id;
    private String name;
    private int age;
    private Team team;
    private String pos;
    private int backNumber;


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setBackNumber(int backNumber) {
        this.backNumber = backNumber;
    }

    public Player(long id, String name, int age, Team team, String pos, int backNumber){
        this.id = id;
        this.name = name;
        this.age = age;
        this.team = team;
        this.pos = pos;
        this.backNumber = backNumber;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", team=" + team +
                ", pos='" + pos + '\'' +
                ", backNumber=" + backNumber +
                '}';
    }

    public long getId() {
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
