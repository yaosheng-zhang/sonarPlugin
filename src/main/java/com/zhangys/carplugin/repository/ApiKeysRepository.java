package com.zhangys.carplugin.repository;

import com.zhangys.carplugin.Entity.Po.ApiKeys;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ApiKeysRepository extends CrudRepository<ApiKeys, String> {

       @Query(value = "SELECT c.key FROM ApiKeys c")
       List<String>  findAllKeys();

}
