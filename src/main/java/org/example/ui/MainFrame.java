package org.example.ui;

import org.example.domain.Player;
import org.example.domain.Team;
import org.example.service.PlayerService;
import org.example.service.TeamService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainFrame extends JFrame {


    TeamService ts = new TeamService();
    PlayerService ps = new PlayerService();

    private final DefaultTableModel leftModel;
    private final DefaultTableModel rightModel;
    private final JTable leftTable;
    private final JTable rightTable;

    private final JButton leftAddBtn   = new JButton("등록");
    private final JButton leftDelBtn   = new JButton("삭제");

    private final JButton rightAddBtn  = new JButton("추가");
    private final JButton rightDelBtn  = new JButton("삭제");


    private final JButton rightDetailBtn = new JButton("상세보기");


    public MainFrame(){
        super("하키매니저");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 560);
        setLocationRelativeTo(null);

        // 왼쪽(플레이어) 테이블: ID, 이름, 나이, 팀, 포지션, 등번호
        leftModel  = new DefaultTableModel(new Object[]{"ID","이름","나이","팀","포지션","등번호"}, 0);
        leftTable  = new JTable(leftModel);

        // 오른쪽(팀 예시) 테이블
        rightModel = new DefaultTableModel(new Object[]{"ID","이름","지역"}, 0);
        rightTable = new JTable(rightModel);

        JScrollPane leftScroll  = new JScrollPane(leftTable);
        JScrollPane rightScroll = new JScrollPane(rightTable);

        JPanel leftButtons  = makeButtonBar(leftAddBtn, leftDelBtn);
        JPanel rightButtons = makeButtonBar(rightAddBtn,rightDelBtn, rightDetailBtn);

        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.add(leftScroll, BorderLayout.CENTER);
        leftPanel.add(leftButtons, BorderLayout.SOUTH);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));

        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.add(rightScroll, BorderLayout.CENTER);
        rightPanel.add(rightButtons, BorderLayout.SOUTH);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 8));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.55);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);

        loadTeamsFromDb();
        loadPlayersFromDb();


//        // 데모 데이터
//        setLeftRows(new Object[][]{
//                {1, "김철수", 24, "Tigers", "C",  9},
//                {2, "이영희", 29, "Bears",  "LW", 11},
//                {3, "박현수", 27, "Sharks", "D",  77}
//        });
//        setRightRows(new Object[][]{
//                {1, "SeoulTiger", "서울"}, {2, "BusanBear", "부산"}, {3, "IncheonEagle", "인천"}
//        });

        // 왼쪽 등록 버튼: 다이얼로그 띄워서 입력받고 한 번에 추가
        leftAddBtn.addActionListener(e -> {
            PlayerDialog dlg = new PlayerDialog(this);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                Object[] row = new Object[]{
                        dlg.getId(),
                        dlg.getName(),
                        dlg.getAge(),
                        dlg.getTeam(),
                        dlg.getPosition(),
                        dlg.getJersey()
                };
                Player player = new Player(dlg.getId(),dlg.getName(),dlg.getAge(),ts.findName(dlg.getTeam()), dlg.getPosition(), dlg.getJersey());
                ps.save(player);
                leftModel.addRow(row);

            }
            loadPlayersFromDb();
        });


        //

        rightAddBtn.addActionListener(e -> {
            TeamDialog dlg = new TeamDialog(this);
            dlg.setVisible(true);
            if (!dlg.isConfirmed()) return;

            // 도메인 객체 생성 후 Service 저장
            Team team = new Team(
                    dlg.getTeamId(),
                    dlg.getTeamName(),
                    dlg.getRegion()
            );
            try {
                ts.save(team); // ← TeamService.insert/ save
                // UI 테이블에도 반영
                rightModel.addRow(new Object[]{ dlg.getTeamId(), dlg.getTeamName(), dlg.getLocation() });
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "팀 저장 중 오류: " + ex.getMessage());

            }

            loadTeamsFromDb();

        })


        ;


        // (참고용) 나머지 버튼은 필요 시 연결하세요

        leftDelBtn.addActionListener(e -> {
            int r = leftTable.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "왼쪽 테이블에서 행을 선택하세요.");
                return;
            }
            Long id = (Long) leftModel.getValueAt(r, 0);
            Player player = ps.findById(id);

            int confirm = JOptionPane.showConfirmDialog(
                    this, "플레이어(ID=" + id + ")를 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                ps.delete(player);              // ← PlayerService.delete(id)
                leftModel.removeRow(r);     // UI 반영
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "플레이어 삭제 중 오류: " + ex.getMessage());
            }
            loadPlayersFromDb();

        });

        rightDelBtn.addActionListener(e -> {
            int r = rightTable.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "오른쪽 테이블에서 행을 선택하세요.");
                return;
            }
            int id = (Integer) rightModel.getValueAt(r, 0);
            Team team = ts.find(id);

            int confirm = JOptionPane.showConfirmDialog(
                    this, "팀(ID=" + id + ")을 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                ts.delete(team);              // ← TeamService.delete(id)
                rightModel.removeRow(r);    // UI 반영
            } catch (Exception ex) {
                // 외래키 제약(해당 팀에 소속된 플레이어 존재 등)으로 실패할 수 있음
                JOptionPane.showMessageDialog(this, "팀 삭제 중 오류: " + ex.getMessage());
            }
            loadTeamsFromDb();

        });


        rightDetailBtn.addActionListener(e -> {
            int r = rightTable.getSelectedRow();
            if (r < 0) {
                JOptionPane.showMessageDialog(this, "오른쪽 테이블에서 팀을 선택하세요.");
                return;
            }
            int teamId = (int)rightModel.getValueAt(r, 0); // 컬럼: ID(0), 이름(1), 지역(2)

            java.util.List<Player> players;
            try {
                players = ps.findByTeamId(teamId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "플레이어 조회 오류: " + ex.getMessage());
                return;
            }

            new PlayersOfTeamDialog(this, teamId, players).setVisible(true);
        });
    }





    private JPanel makeButtonBar(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        for (JButton b : buttons) {
            b.setFocusPainted(false);
            bar.add(b);
        }
        return bar;
    }

    public void setLeftRows(Object[][] rows) {
        leftModel.setRowCount(0);
        for (Object[] r : rows) leftModel.addRow(r);
    }
    public void setRightRows(Object[][] rows) {
        rightModel.setRowCount(0);
        for (Object[] r : rows) rightModel.addRow(r);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    static class PlayersOfTeamDialog extends JDialog {
        PlayersOfTeamDialog(Frame owner, int teamId, java.util.List<Player> players) {
            super(owner, "팀 상세보기 - " + teamId, true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // 테이블 모델
            String[] cols = {"ID","이름","나이","포지션","등번호"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            for (Player p : players) {
                model.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        p.getAge(),
                        p.getPos(),
                        p.getBackNumber()   // 필드명이 다르면 맞게 변경
                });
            }

            JTable table = new JTable(model);
            JScrollPane scroll = new JScrollPane(table);

            JPanel root = new JPanel(new BorderLayout(8,8));
            root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
            root.add(scroll, BorderLayout.CENTER);

            setContentPane(root);
            setSize(520, 360);   // 작은 창
            setLocationRelativeTo(owner);
        }
    }


    /** ===== 플레이어 등록용 작은 입력 창 (버튼 1개만) ===== */
    static class PlayerDialog extends JDialog {
        private final JTextField tfId     = new JTextField(10);
        private final JTextField tfName   = new JTextField(12);
        private final JSpinner   spAge    = new JSpinner(new SpinnerNumberModel(20, 1, 120, 1));
        private final JTextField tfTeam   = new JTextField(12);
        private final JComboBox<String> cbPos =
                new JComboBox<>(new String[]{"C","LW","RW","D","G"});
        private final JSpinner   spJersey = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        private final JButton    btnSubmit = new JButton("등록");

        private boolean confirmed = false;

        PlayerDialog(Frame owner) {
            super(owner, "플레이어 등록", true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // 폼 구성: 라벨-컴포넌트 2열
            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(4, 6, 4, 6);
            gc.anchor = GridBagConstraints.WEST;

            int r = 0;
            addRow(form, gc, r++, "ID", tfId);
            addRow(form, gc, r++, "이름", tfName);
            addRow(form, gc, r++, "나이", spAge);
            addRow(form, gc, r++, "팀", tfTeam);
            cbPos.setPrototypeDisplayValue("RW"); // 콤보 기본 폭 안정화
            addRow(form, gc, r++, "포지션", cbPos);
            addRow(form, gc, r++, "등번호", spJersey);

            // 아래 버튼 한 개만 중앙 정렬
            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
            bottom.add(btnSubmit);

            getRootPane().setDefaultButton(btnSubmit); // 엔터로 등록

            btnSubmit.addActionListener(e -> {
                if (!validateInputs()) return;
                confirmed = true;
                dispose();
            });

            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
            wrap.add(form, BorderLayout.CENTER);
            wrap.add(bottom, BorderLayout.SOUTH);

            setContentPane(wrap);
            pack();
            setResizable(false);
            setLocationRelativeTo(owner);
        }




        private void addRow(JPanel panel, GridBagConstraints gc, int row, String label, JComponent comp) {
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0; gc.fill = GridBagConstraints.NONE;
            panel.add(new JLabel(label), gc);
            gc.gridx = 1; gc.gridy = row; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comp, gc);
        }

        private boolean validateInputs() {
            // ID 정수, 이름/팀 비어있지 않음
            try {
                Integer.parseInt(tfId.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "ID는 숫자로 입력해주세요.");
                tfId.requestFocus();
                return false;
            }
            if (tfName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "이름을 입력해주세요.");
                tfName.requestFocus();
                return false;
            }
            if (tfTeam.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "팀을 입력해주세요.");
                tfTeam.requestFocus();
                return false;
            }
            return true;
        }

        // 결과 읽기용 getter
        public boolean isConfirmed() { return confirmed; }
        public int getId() { return Integer.parseInt(tfId.getText().trim()); }
        public String getName() { return tfName.getText().trim(); }
        public int getAge() { return (Integer) spAge.getValue(); }
        public String getTeam() { return tfTeam.getText().trim(); }
        public String getPosition() { return (String) cbPos.getSelectedItem(); }
        public int getJersey() { return (Integer) spJersey.getValue(); }
    }


    static class TeamDialog extends JDialog {
        private final JTextField tfTeamId   = new JTextField(10);
        private final JTextField tfTeamName = new JTextField(12);
        private final JTextField tfRegion   = new JTextField(12);
        private final JButton    btnSubmit  = new JButton("등록");

        private boolean confirmed = false;

        TeamDialog(Frame owner) {
            super(owner, "팀 등록", true);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JPanel form = new JPanel(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(4, 6, 4, 6);
            gc.anchor = GridBagConstraints.WEST;

            int r = 0;
            addRow(form, gc, r++, "팀 아이디", tfTeamId);
            addRow(form, gc, r++, "이름", tfTeamName);
            addRow(form, gc, r++, "지역", tfRegion);

            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 10));
            bottom.add(btnSubmit);
            getRootPane().setDefaultButton(btnSubmit);

            btnSubmit.addActionListener(e -> {
                if (!validateInputs()) return;
                confirmed = true;
                dispose();
            });

            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 6, 10));
            wrap.add(form, BorderLayout.CENTER);
            wrap.add(bottom, BorderLayout.SOUTH);

            setContentPane(wrap);
            pack();
            setResizable(false);
            setLocationRelativeTo(owner);
        }

        private void addRow(JPanel panel, GridBagConstraints gc, int row, String label, JComponent comp) {
            gc.gridx = 0; gc.gridy = row; gc.weightx = 0; gc.fill = GridBagConstraints.NONE;
            panel.add(new JLabel(label), gc);
            gc.gridx = 1; gc.gridy = row; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comp, gc);
        }

        private boolean validateInputs() {
            try {
                Integer.parseInt(tfTeamId.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "팀 아이디는 숫자로 입력해주세요.");
                tfTeamId.requestFocus();
                return false;
            }
            if (tfTeamName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "팀 이름을 입력해주세요.");
                tfTeamName.requestFocus();
                return false;
            }
            if (tfRegion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "지역을 입력해주세요.");
                tfRegion.requestFocus();
                return false;
            }
            return true;
        }

        public boolean isConfirmed() { return confirmed; }
        public int getTeamId() { return Integer.parseInt(tfTeamId.getText().trim()); }
        public String getTeamName() { return tfTeamName.getText().trim(); }
        public String getRegion() { return tfRegion.getText().trim(); }
    }

    // 팀 테이블 로드
    private void loadTeamsFromDb() {
        rightModel.setRowCount(0);
        try {
            // ts.findAll() 또는 ts.findAllTeams() 등 프로젝트 메서드명에 맞게 변경
            for (Team t : ts.findAll()) {
                rightModel.addRow(new Object[]{ t.getId(), t.getTeamName(), t.getLocation() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "팀 로드 오류: " + ex.getMessage());
        }
    }

    // 플레이어 테이블 로드
    private void loadPlayersFromDb() {
        leftModel.setRowCount(0);
        try {
            // ps.findAll() 등 메서드명에 맞게 변경
            for (Player p : ps.findAll()) {
                // Player에 Team 객체가 있다면:
                String teamName = (p.getTeam() != null) ? p.getTeam().getTeamName() : "";
                leftModel.addRow(new Object[]{
                        p.getId(), p.getName(), p.getAge(), teamName, p.getPos(), p.getBackNumber()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "플레이어 로드 오류: " + ex.getMessage());
        }
    }

}