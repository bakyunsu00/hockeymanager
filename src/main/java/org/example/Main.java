package org.example;

import org.example.domain.Player;
import org.example.domain.Team;
import org.example.service.PlayerService;

public class Main {
    public static void main(String[] args) {


        PlayerService ps = new PlayerService();
        Team team = new Team(1,"킹콩스타","수원");
        Player player = new Player(1L,"박현수",29,team,"C",21);

        System.out.println(ps.delete(player));
        ps.save(player);
        System.out.println(ps.findById(1L));


        }
    }
