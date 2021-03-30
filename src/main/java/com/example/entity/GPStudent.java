package com.example.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class GPStudent {
    Integer id;//默认id
    String number;//Stu+id
    String name;//姓名
    Integer age;//年龄
    String id_no;//身份证号
    String payment;//缴费情况
    String status;//练车状态
    String email;//邮箱
    String phone;//联系方式
    String coach;//教练
    String remark;//备注
    Integer subject_1;//科目一的分数
    Integer subject_2;//科目二的分数
    Integer subject_3;//科目三的分数
    Integer subject_4;//科目四的分数
    String typeOfClass;//驾照类型
    Timestamp create_at;//创建时间
    Timestamp update_at;//更新时间

    public Integer getSubject_1() {
        return subject_1;
    }

    public void setSubject_1(Integer subject_1) {
        this.subject_1 = subject_1;
    }

    public Integer getSubject_2() {
        return subject_2;
    }

    public void setSubject_2(Integer subject_2) {
        this.subject_2 = subject_2;
    }

    public Integer getSubject_3() {
        return subject_3;
    }

    public void setSubject_3(Integer subject_3) {
        this.subject_3 = subject_3;
    }

    public Integer getSubject_4() {
        return subject_4;
    }

    public void setSubject_4(Integer subject_4) {
        this.subject_4 = subject_4;
    }

    public String getTypeOfClass() {
        return typeOfClass;
    }

    public void setTypeOfClass(String typeOfClass) {
        this.typeOfClass = typeOfClass;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    public Timestamp getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Timestamp update_at) {
        this.update_at = update_at;
    }


}
