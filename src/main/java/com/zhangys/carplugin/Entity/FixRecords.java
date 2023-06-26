package com.zhangys.carplugin.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "fixrecords")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FixRecords {
    @Id
    private String id;

    @Column(name = "origin_code", nullable = false)
    private  String originCode;

    @Column(name = "post_code", nullable = false)
    private  String postCode;

    @Column(name = "diff_code", nullable = false)
    private  String diffCode;

    @Column(name = "project_name", nullable = false)
    private  String projectName;

    @Column(name = "message", nullable = false)
    private  String message;


    @Column(name = "create_time", nullable = false)
    private LocalDate createTime;
}
