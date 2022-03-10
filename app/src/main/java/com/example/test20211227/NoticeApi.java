package com.example.test20211227;

import android.net.TrafficStats;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class NoticeApi {
    boolean inCount = false, inOccr_date = false, inOccr_time = false, inExp_clr_date = false, inExp_clr_time = false;
    boolean inAcc_type = false, inGrs80tm_x = false, inGrs80tm_y = false, inAcc_info = false;
    String occr_date = null, occr_time = null, exp_clr_date = null, exp_clr_time = null;
    String acc_type = null, grs80tm_x = null, grs80tm_y = null, acc_info = null;
    boolean inAcc_id = false; String acc_id = null;
    String apiUrl;
    Statement stmt;
    
    // Update 메소드를 사용하여 db 내용을 update함
    public NoticeApi(String url) {
        try {
            stmt = MainActivity_logout.connection.createStatement();
            apiUrl = url;
            update();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // API를 활용하여 db 내용을 Update하는 메소드( 전체 삭제 후 전체 추가)
    public void update(){
        String sql = "delete from notice";
        PreparedStatement psmt;
        try {
            psmt = MainActivity_logout.connection.prepareStatement(sql);
            psmt.execute();
            parsing();
        }catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // API -> XML -> Android -> DB 넣는 메소드
    public void parsing(){
        Notice notice = new Notice();
        try {
            URL url = new URL(this.apiUrl);
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();
            parser.setInput(url.openStream(), null);
            TrafficStats.setThreadStatsTag((int) Thread.currentThread().getId());
            int parserEvent = parser.getEventType();
            while(parserEvent != XmlPullParser.END_DOCUMENT){

                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("acc_id")){ //title 만나면 내용을 받을수 있게 하자
                            inAcc_id = true;
                        }
                        if(parser.getName().equals("occr_date")){ //title 만나면 내용을 받을수 있게 하자
                            inOccr_date = true;
                        }
                        if(parser.getName().equals("occr_time")){ //address 만나면 내용을 받을수 있게 하자
                            inOccr_time = true;
                        }
                        if(parser.getName().equals("exp_clr_date")){ //mapx 만나면 내용을 받을수 있게 하자
                            inExp_clr_date = true;
                        }
                        if(parser.getName().equals("exp_clr_time")){ //mapy 만나면 내용을 받을수 있게 하자
                            inExp_clr_time = true;
                        }
                        if(parser.getName().equals("acc_type")){ //mapy 만나면 내용을 받을수 있게 하자
                            inAcc_type = true;
                        }
                        if(parser.getName().equals("grs80tm_x")){ //mapy 만나면 내용을 받을수 있게 하자
                            inGrs80tm_x = true;
                        }
                        if(parser.getName().equals("grs80tm_y")){ //mapy 만나면 내용을 받을수 있게 하자
                            inGrs80tm_y = true;
                        }
                        if(parser.getName().equals("acc_info")){ //mapy 만나면 내용을 받을수 있게 하자
                            inAcc_info = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inAcc_id){ //isTitle이 true일 때 태그의 내용을 저장.
                            notice.setAcc_id(parser.getText());
                            inAcc_id = false;
                        }
                        if(inOccr_date){ //isTitle이 true일 때 태그의 내용을 저장.
                            notice.setOccr_date(parser.getText());
                            inOccr_date = false;
                        }
                        if(inOccr_time){ //isAddress이 true일 때 태그의 내용을 저장.
                            occr_time = parser.getText();
                            StringBuffer a= new StringBuffer();
                            a.append(occr_time.substring(0,2)).append(':').append(occr_time.substring(2,4)).append(":00");
                            notice.setOccr_time(a.toString());
                            inOccr_time = false;
                        }
                        if(inExp_clr_date){
                            notice.setExp_clr_date(parser.getText());
                            inExp_clr_date = false;
                        }
                        if(inExp_clr_time){
                            exp_clr_time = parser.getText();
                            StringBuffer a= new StringBuffer();
                            a.append(exp_clr_time.substring(0,2)).append(':').append(exp_clr_time.substring(2,4)).append(":00");
                            notice.setExp_clr_time(a.toString());
                            inExp_clr_time = false;
                        }
                        if(inAcc_type){
                            notice.setAcc_type(parser.getText());
                            inAcc_type = false;
                        }
                        if(inGrs80tm_x){
                            notice.setGrs80tm_x(parser.getText());
                            inGrs80tm_x = false;
                        }
                        if(inGrs80tm_y){
                            notice.setGrs80tm_y(parser.getText());
                            inGrs80tm_y = false;
                        }
                        if(inAcc_info){
                            notice.setAcc_info(parser.getText());
                            inAcc_info = false;
                            notice.insert(MainActivity_logout.connection);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                parserEvent = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    // DB에서 원하는 column 추출하기 (매개 변수로 column명을 입력하면 내용 return
    public List<String> select(String column){
        List<String> result = new ArrayList<String>();
        try {
            String selectSql = "select "+ column + " from notice";
            ResultSet rs = stmt.executeQuery(selectSql);
            while(rs.next()){
                result.add(rs.getString(column));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 내용 검색할 수 있는 메소드 (원하는 column명과 내용을 입력하면 내용이 있는 행의 column값 출력)
    public ArrayList<String> selectCondition(String column, String Condition){
        ArrayList<String> result = new ArrayList<String>();
        try {
            String selectSql = "select "+ column + " from notice where acc_info like N'%" + Condition + "%'";
            ResultSet rs = stmt.executeQuery(selectSql);
            while(rs.next()){
                result.add(rs.getString(column));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    // MainActivity에서 List<Notice> 변수 = NoticeApi객체.select();
    // DB에 있는 모든 내용을 List<Notice>로 출력
    public ArrayList<Notice> select(){
        ArrayList<Notice> lists= new ArrayList<Notice>();
        String Search = "select * from notice";
        ResultSet rs = null;
        try {
            stmt = MainActivity_logout    .connection.createStatement();
            rs = stmt.executeQuery(Search);
            while(rs.next()){
                Notice notice = new Notice(rs.getString("Acc_id"),rs.getString("occr_date"),
                        rs.getString("occr_time"),rs.getString("exp_clr_date"),rs.getString("exp_clr_time"),
                        rs.getString("acc_type"),rs.getString("grs80tm_x"),rs.getString("grs80tm_y"),rs.getString("acc_info"));
                lists.add(notice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lists;
    }
}
