package com.example.test20211227;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

class Notice implements Serializable {
    private String acc_id;
    private String occr_date;
    private String occr_time;
    private String exp_clr_date;
    private String exp_clr_time;
    private String acc_type;
    private String grs80tm_x;
    private String grs80tm_y;
    private String acc_info;

    public int getAcc_id() {
        return Integer.parseInt(acc_id);
    }

    public void setAcc_id(String acc_id) {
        this.acc_id = acc_id;
    }

    public String getOccr_time() {
        return occr_time;
    }

    public void setOccr_time(String occr_time) {
        this.occr_time = occr_time;
    }

    public String getExp_clr_time() {
        return exp_clr_time;
    }

    public void setExp_clr_time(String exp_clr_time) {
        this.exp_clr_time = exp_clr_time;
    }

    public String getAcc_type() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }

    public String getAcc_info() {
        return acc_info;
    }

    public void setAcc_info(String acc_info) {
        this.acc_info = acc_info;
    }

    public String getGrs80tm_x() {
        return grs80tm_x;
    }

    public void setGrs80tm_x(String grs80tm_x) {
        this.grs80tm_x = grs80tm_x;
    }

    public String getGrs80tm_y() {
        return grs80tm_y;
    }

    public void setGrs80tm_y(String grs80tm_y) {
        this.grs80tm_y = grs80tm_y;
    }

    public String getOccr_date() {
        return occr_date;
    }

    public void setOccr_date(String occr_date) {
        this.occr_date = occr_date;
    }

    public String getExp_clr_date() {
        return exp_clr_date;
    }

    public void setExp_clr_date(String exp_clr_date) {
        this.exp_clr_date = exp_clr_date;
    }


    public Notice(String acc_id, String occr_date, String occr_time, String exp_clr_date, String exp_clr_time, String acc_type, String grs80tm_x, String grs80tm_y, String acc_info) {
        this.acc_id = acc_id;
        this.occr_date = occr_date;
        this.occr_time = occr_time;
        this.exp_clr_date = exp_clr_date;
        this.exp_clr_time = exp_clr_time;
        this.acc_type = acc_type;
        this.grs80tm_x = grs80tm_x;
        this.grs80tm_y = grs80tm_y;
        this.acc_info = acc_info;
    }

    public Notice() {
        this.acc_id = "";
        this.occr_date = "";
        this.occr_time = "";
        this.exp_clr_date = "";
        this.exp_clr_time = "";
        this.acc_type = "";
        this.grs80tm_x = "";
        this.grs80tm_y = "";
        this.acc_info = "";
    }

    public boolean equals(Notice o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notice notice = (Notice) o;
        return Objects.equals(notice.acc_id, acc_id) && Objects.equals(notice.grs80tm_x, grs80tm_x) && Objects.equals(notice.grs80tm_y, grs80tm_y) && Objects.equals(occr_date, notice.occr_date) && Objects.equals(occr_time, notice.occr_time) && Objects.equals(exp_clr_date, notice.exp_clr_date) && Objects.equals(exp_clr_time, notice.exp_clr_time) && Objects.equals(acc_type, notice.acc_type) && Objects.equals(acc_info, notice.acc_info);
    }

    @Override
    public String toString() {
        return "Notice{" +
                "acc_id=" + acc_id + '\n' +
                "occr_date=" + occr_date + '\n' +
                ", occr_time=" + occr_time + '\n' +
                ", exp_clr_date=" + exp_clr_date + '\n' +
                ", exp_clr_time=" + exp_clr_time + '\n' +
                ", acc_type=" + acc_type + '\n' +
                ", grs80tm_x=" + grs80tm_x + '\n' +
                ", grs80tm_y=" + grs80tm_y + '\n' +
                ", acc_info=" + acc_info + "\n } \n";
    }

    // DB에 공지사항 1개를 추가하는 메소드
    public void insert(Connection con) {
        if(! find(con) ) {
            String InsertSQL = "insert into notice values (?,convert(date,?),convert(time,?),convert(date,?),convert(time,?),?,?,?,?)";
            PreparedStatement ist;
            try {
                ist = con.prepareStatement(InsertSQL);
                ist.setInt(1, this.getAcc_id());
                ist.setString(2, this.getOccr_date());
                ist.setString(3, this.getOccr_time());
                ist.setString(4, this.getExp_clr_date());
                ist.setString(5, this.getExp_clr_time());
                ist.setString(6, this.getAcc_type());
                ist.setFloat(7, Float.parseFloat(this.getGrs80tm_x()));
                ist.setFloat(8, Float.parseFloat(this.getGrs80tm_y()));
                ist.setString(9, this.getAcc_info());

                ist.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("오류123");
            }
        }
    }
    
    // DB에 해당 공지사항이 존재여부를 확인하는 메소드
    public boolean find(Connection con) {
        String SearchSQL = "select * from notice where acc_id = " + this.getAcc_id();
        PreparedStatement sst = null;
        try {
            sst = con.prepareStatement(SearchSQL);
            ResultSet rs = sst.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

}
