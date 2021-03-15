package com.example.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Data
@Getter
@Setter
public class GPCar {
    private Integer Id;//编号
    private String VIN;//车架号
    private String CarNumber;//车牌号
    private String ChargeMan;//负责人
    private Integer Status;//状态
    private String Brand;//品牌
    private Timestamp create_at;//信息创建时间
    private Timestamp update_at;//信息修改时间

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String carNumber) {
        CarNumber = carNumber;
    }

    public String getChargeMan() {
        return ChargeMan;
    }

    public void setChargeMan(String chargeMan) {
        ChargeMan = chargeMan;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer Status) {
        this.Status = Status;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
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
