package com.example.test20211227;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

class Member implements Serializable {
    private String ID;
    private String PWD;
    private String NAME;
    private String PhoneNumber;
    private String birth;
    private String gender;
    private int age;
    private String email;

    public Member(String ID, String PWD, String NAME, String PhoneNumber, String birth, String gender, int age, String email) {
        this.ID = ID;
        this.PWD = PWD;
        this.NAME = NAME;
        this.PhoneNumber = PhoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.age = age;
        this.email = email;
    }

    public Member() {
        this.ID = "";
        this.PWD = "";
        this.NAME = "";
        this.PhoneNumber = "";
        this.birth = "";
        this.gender = "";
        this.age = 0;
        this.email = "";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPWD() {
        return PWD;
    }

    public void setPWD(String PWD) {
        this.PWD = PWD;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    //    public boolean equals(Member o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Member member = (Member) o;
//        return Objects.equals(member.ID, ID) && Objects.equals(member.PWD, PWD) && Objects.equals(member.NAME, NAME) && Objects.equals(member.PhoneNumber, PhoneNumber) && Objects.equals(member.birth, birth) && Objects.equals(member.gender, gender) && Objects.equals(member.age, age) && Objects.equals(member.email, email);
//    }

    @Override
    public String toString() {
        return "Member{" +
                "ID=" + ID + '\n' +
                "PWD=" + PWD + '\n' +
                "NAME=" + NAME + '\n' +
                "PhoneNumber=" + PhoneNumber + '\n' +
                "birth=" + birth + '\n' +
                "gender=" + gender + '\n' +
                "age=" + age + '\n' +
                "email=" + email + "\n } \n";

    }

    // DB에 공지사항 1개를 추가하는 메소드
    public void insert(Connection con) {
        if(! find(con,this.getID()) ) {
            String InsertSQL = "insert into member values (?,?,?,?,?,?,?,?)";
            PreparedStatement ist;
            try {
                ist = con.prepareStatement(InsertSQL);
                ist.setString(1, this.getID());
                ist.setString(2, this.getPWD());
                ist.setString(3, this.getNAME());
                ist.setString(4, this.getPhoneNumber());
                ist.setString(5, this.getBirth());
                ist.setString(6, this.getGender());
                ist.setInt(7, this.getAge());
                ist.setString(8, this.getEmail());

                ist.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("오류123");
            }
        }
    }
    public Member select(Connection con,String ID) {
        String SearchSQL = "select * from member where ID = '" + ID +"'";
        PreparedStatement sst = null;
        Member member = new Member();
        try {
            sst = con.prepareStatement(SearchSQL);
            ResultSet rs = sst.executeQuery();
            if (rs.next()) {
                member.setID(ID);
                member.setPWD(rs.getString("pwd"));
                member.setNAME(rs.getString("NAME"));
                member.setPhoneNumber(rs.getString("Phonenumber"));
                member.setBirth(rs.getString("birth"));
                member.setGender(rs.getString("Gender"));
                member.setAge(rs.getInt("age"));
                member.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return member;
    }
    // DB에 해당 공지사항이 존재여부를 확인하는 메소드
    public boolean find(Connection con,String ID) {
        String SearchSQL = "select * from member where ID = '" + ID +"'";
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

    public boolean isNUll(){
        if(getID().equals("")){
            return true;
        }else if(getPWD().equals("")){
            return true;
        }else if(getPhoneNumber().equals("")){
            return true;
        }else if(getNAME().equals("")){
            return true;
        }else if(getBirth().equals("")){
            return true;
        }else if(getGender().equals("")){
            return true;
        }else if(getAge()==0){
            return true;
        }else{
            return false;
        }
    }
    public void updateMember(Connection con){
        String updateSQL = "UPDATE member SET name = ?, phonenumber = ?,birth = ?,gender= ?,age = ?,email = ? where ID = ?";
        PreparedStatement ist;
        try {
            ist = con.prepareStatement(updateSQL);
            ist.setString(1, this.getNAME());
            ist.setString(2, this.getPhoneNumber());
            ist.setString(3, this.getBirth());
            ist.setString(4, this.getGender());
            ist.setInt(5, this.getAge());
            ist.setString(6, this.getEmail());
            ist.setString(7, this.getID());

            ist.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("오류");
        }
    }
    public void updatePassword(Connection con){
        String updateSQL = "UPDATE member SET pwd = ? where ID = ?";
        PreparedStatement ist;
        try {
            ist = con.prepareStatement(updateSQL);
            ist.setString(1, this.getPWD());
            ist.setString(2, this.getID());
            ist.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("오류");
        }
    }

}
