package com.zhangys.carplugin.Entity.Po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Xzzz
 * @data 2023/06/29
 */
@Data
@Entity
public class ApiKeys {
    @Id
    private String key;

    @Column(name = "owner", nullable = false)
    private String owner;
}
