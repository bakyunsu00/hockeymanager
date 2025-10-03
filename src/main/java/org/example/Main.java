package org.example;

import org.example.domain.Player;
import org.example.domain.Team;
import org.example.service.PlayerService;

public class Main {
    public static void main(String[] args) {


        PlayerService ps = new PlayerService();
        Team team = new Team(1,"킹콩스타","수원");
        Player player = new Player(1L,"박현수",29,team,"C",21);
        Player player2 = new Player(2L,"김옥지",29,team,"C",22);
        Player player3 = new Player(3L,"김빵빵",29,team,"C",23);
        System.out.println(ps.delete(player));
        System.out.println(ps.delete(player2));
        System.out.println(ps.delete(player3));
        ps.save(player);
        ps.save(player2);
        ps.save(player3);


        for(Player temp: ps.findAll()){
            System.out.println(temp);
        }


        }
    }
