
/*
package Gamelogic;
import Database.*;
import com.mysql.cj.protocol.Resultset;

//import java.sql.*;
//import java.util.ArrayList;
//
//public class Game2 {
//    private GameMethods2 methods2 = new GameMethods2();
//
//
//
//    public void setInitiative(){
//        Connection connection = null;
//        PreparedStatement sentence = null;
//        ResultSet res = null;
//        ArrayList<Integer> initiative = new ArrayList<>();
//        try{
//            connection = DriverManager.getConnection(dbname);
//            String sql = "select player_id from game_lobby";
//            sentence = connection.prepareStatement(sql);
//            res = sentence.executeQuery();
//            while(res.next()){
//                int nr = res.getInt("player_id");
//                initiative.add(nr);
//            }
//
//            sql = "select lobby_key from game_lobby";
//            sentence = connection.prepareStatement(sql);
//            res = sentence.executeQuery();
//            int lobby_key = res.getInt("lobby_key");
//
//            sql = "select monster_id from monster";
//            sentence = connection.prepareStatement(sql);
//            res = sentence.executeQuery();
//            int monsterNr = res.getInt("monster_id");
//            initiative.add(monsterNr);
//
//
//            ArrayList sorted = methods2.initiativeSort(initiative);
//            for(int i = 0; i < sorted.size(); i++){
//                int id = (int) sorted.get(i);
//
//                sql = "insert into initiative(lobby_key, player_id, initiative_turn) values(?,?,?)";
//                sentence = connection.prepareStatement(sql);
//                sentence.setInt(1,lobby_key);
//                sentence.setInt(2,id);
//                sentence.setInt(3,i+1);
//                sentence.executeUpdate();
//            }
//
//        }catch(SQLException e){
//
//        }finally{
//
//        }
//    }
//
//    public void play(){
//        try{
//            connection = DriverManager.getConnection(dbname);
//            String sql = "select count(player_id) as number from initiative";
//            sentence = connection.prepareStatement(sql);
//            res = sentence.executeQuery();
//            int count = res.getInt("number");
//            sql = "select turn from game_lobby";
//            sentence = connection.prepareStatement(sql);
//            res = sentence.executeQuery();
//            int turn = res.getInt("turn");
//            if(turn > count){
//                sql = "insert into Game_lobby(turn) values(?)";
//                sentence = connection.prepareStatement(sql);
//                sentence.setInt(1,0);
//                sentence.executeUpdate();
//                sql = "select turn from game_lobby";
//                sentence = connection.prepareStatement(sql);
//                res = sentence.executeQuery();
//                turn = res.getInt("turn");
//            }
//            sql = "Select ";
//
//        }
//    }
//}
//




<<<<<<< HEAD
=======
    public void setInitiative(){
        Connection connection = null;
        PreparedStatement sentence = null;
        ResultSet res = null;
        ArrayList<Integer> initiative = new ArrayList<>();
        try{
            connection = DriverManager.getConnection(dbname);
            String sql = "select player_id from game_lobby";
            sentence = connection.prepareStatement(sql);
            res = sentence.executeQuery();
            while(res.next()){
                int nr = res.getInt("player_id");
                initiative.add(nr);
            }

            sql = "select lobby_key from game_lobby";
            sentence = connection.prepareStatement(sql);
            res = sentence.executeQuery();
            int lobby_key = res.getInt("lobby_key");

            sql = "select monster_id from monster";
            sentence = connection.prepareStatement(sql);
            res = sentence.executeQuery();
            int monsterNr = res.getInt("monster_id");
            initiative.add(monsterNr);


            ArrayList sorted = methods2.initiativeSort(initiative);
            for(int i = 0; i < sorted.size(); i++){
                int id = (int) sorted.get(i);

                sql = "insert into initiative(lobby_key, player_id, initiative_turn) values(?,?,?)";
                sentence = connection.prepareStatement(sql);
                sentence.setInt(1,lobby_key);
                sentence.setInt(2,id);
                sentence.setInt(3,i+1);
                sentence.executeUpdate();
            }

        }catch(SQLException e){

        }finally{

        }
    }

    public void play(){
        try{
            connection = DriverManager.getConnection(dbname);
            String sql = "select count(player_id) as number from initiative";
            sentence = connection.prepareStatement(sql);
            res = sentence.executeQuery();
            int count = res.getInt("number");
            sql = "select turn from game_lobby";
            sentence = connection.prepareStatement(sql);
            res = sentence.executeQuery();
            int turn = res.getInt("turn");
            if(turn > count){
                sql = "insert into Game_lobby(turn) values(?)";
                sentence = connection.prepareStatement(sql);
                sentence.setInt(1,0);
                sentence.executeUpdate();
                sql = "select turn from game_lobby";
                sentence = connection.prepareStatement(sql);
                res = sentence.executeQuery();
                turn = res.getInt("turn");
            }
            sql = "Select "

        }
    }
}
*/

