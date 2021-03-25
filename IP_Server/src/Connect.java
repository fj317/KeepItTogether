import javax.sound.midi.SysexMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Connect {
    private Connection dbConnection;

    public synchronized void Setup(){
        dbConnection = null;

        try{
            Class.forName("org.sqlite.JDBC");

            dbConnection = DriverManager.getConnection("jdbc:sqlite:/home/alex/IdeaProjects/IP_Server/localDatabse.db");


        } catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("error");
        }
    }



    public synchronized String change(String statement) throws SQLException {
        PreparedStatement stmt = dbConnection.prepareStatement(statement);
        stmt.executeUpdate();
        return "1";

    }

    public synchronized ArrayList get(String sql) {
        ArrayList items = new ArrayList();
        try{
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);


            String[] select = getNthWord(sql, 1);
            while(rs.next()){
                //items.add(rs.getString(select));
                String[] current = new String[select.length];
                for (int i = 0; i < select.length; i++){
                    current[i] = rs.getString(select[i]);
                }
                items.add(Arrays.toString(current));
            }



            System.out.println(items);

        }catch(SQLException e){
            System.out.println("error here");
            System.out.println(e);
        }
        return items;
    }


    private synchronized String[] getNthWord(String sql, int n){
        ArrayList<String> broken = new ArrayList<String>();
        String current = "";
        for(int i = 0; i < sql.length(); i++){
            if(sql.charAt(i)  == ' '){
                if(!current.equals("")){
                    broken.add(current);
                    current = "";
                }
            }
            else if(sql.charAt(i) == ','){
                if(!current.equals("")){
                    broken.add(current);
                    current = "";
                }
            }
            else{
                current = current + sql.charAt(i);
            }
        }
        ArrayList<String> words = new ArrayList<String>();
        String check = broken.get(0);
        int pos = 0;
        while (!check.equals("FROM")){
            if(!check.equals("SELECT")){
                words.add(check);
            }
            pos = pos + 1;
            check = broken.get(pos);
        }
        String[] wordsString = new String[words.size()];
        for(int i = 0; i < words.size(); i++){
            wordsString[i] = words.get(i);
        }
        return wordsString;
    }

}