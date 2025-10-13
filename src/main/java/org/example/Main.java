package org.example;

import org.example.domain.Player;
import org.example.domain.Team;
import org.example.service.PlayerService;
import org.example.service.TeamService;
import org.example.ui.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> {new MainFrame().setVisible(true);
        });
        }
    }
